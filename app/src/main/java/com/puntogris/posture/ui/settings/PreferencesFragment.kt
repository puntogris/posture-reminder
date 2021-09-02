package com.puntogris.posture.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.R
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreferencesFragment: PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        preference("username_preference_key") {
            lifecycleScope.launch {
                viewModel.getUserFlow().collect {
                    summary = it.username
                }
            }
            onClick {
                lifecycleScope.launch {
                    val username = viewModel.getUserFlow().first().username
                    val action = SettingsBottomSheetDirections.actionSettingsBottomSheetToDialogName(username)
                    findNavController().navigate(action)
                }
            }
        }

        preference("theme_preference_key"){
            val themeNames = resources.getStringArray(R.array.theme_names)
            lifecycleScope.launch {
                viewModel.getThemeNamePosition().collect {
                    summary = themeNames[it]
                }
            }
            onClick {
                findNavController().navigate(R.id.selectThemeDialog)
            }
        }

        preference("battery_preference_key"){
            setSummary(
                if (isIgnoringBatteryOptimizations()) R.string.all_in_order
                else R.string.require_action
            )
            onClick {
                findNavController().navigate(R.id.batteryOptimizationFragment)
            }
        }
        preference("log_out_preference_key"){
            onClick {
                lifecycleScope.launch {
                    when(viewModel.logOut()){
                        SimpleResult.Failure -> {
                            //showSnackBar(R.string.snack_general_error)
                        }
                        SimpleResult.Success -> {
                            val nav = NavOptions.Builder().setPopUpTo(R.id.navigation, true).build()
                            findNavController().navigate(R.id.loginFragment, null, nav)
                        }
                    }
                }
            }
        }

        preference("credits_preference_key"){
            onClick {
                findNavController().navigate(R.id.creditsBottomSheet)
            }
        }

        preference("ticket_preference_key"){
            onClick {
                findNavController().navigate(R.id.ticketBottomSheet)
            }
        }

        preference("version_preference_key"){
            summary = BuildConfig.VERSION_NAME
            viewModel.setPandaAnimationPref(true)
            //showSnackBar(R.string.snack_panda_unlocked)
        }

        preference("rate_app_preference_key"){
            onClick {
               try {
                   startActivity(
                       Intent(
                           Intent.ACTION_VIEW,
                           Uri.parse("market://details?id=com.puntogris.posture"))
                   )
               }catch (e:Exception){
                   startActivity(
                       Intent(
                           Intent.ACTION_VIEW,
                           Uri.parse("https://play.google.com/store/apps/details?id=" + "com.puntogris.posture")
                       )
                   )
               }
           }
        }
    }
}