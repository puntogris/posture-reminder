package com.puntogris.posture.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.R
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.Constants.BATTERY_PREF_KEY
import com.puntogris.posture.utils.Constants.CREDITS_PREF_KEY
import com.puntogris.posture.utils.Constants.LICENSES_PREF_KEY
import com.puntogris.posture.utils.Constants.LOG_IN_PREF_KEY
import com.puntogris.posture.utils.Constants.LOG_OUT_PREF_KEY
import com.puntogris.posture.utils.Constants.RATE_APP_PREF_KEY
import com.puntogris.posture.utils.Constants.THEME_PREF_KEY
import com.puntogris.posture.utils.Constants.TICKET_PREF_KEY
import com.puntogris.posture.utils.Constants.USERNAME_PREF_KEY
import com.puntogris.posture.utils.Constants.VERSION_PREF_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreferencesFragment: PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        preference(USERNAME_PREF_KEY) {
            lifecycleScope.launch {
                viewModel.getUserFlow().collect {
                    summary = it?.username ?: getString(R.string.human)
                }
            }
            onClick {
                lifecycleScope.launch {
                    val user = viewModel.getUser()
                    if (user == null) {
                        (requireParentFragment() as SettingsBottomSheet)
                            .showSnackBar(
                                message = R.string.snack_action_requires_login,
                                actionText = R.string.action_login
                            ){
                                navigateTo(R.id.internalLoginFragment)
                        }
                    }
                    else{
                        val action = SettingsBottomSheetDirections.actionSettingsBottomSheetToDialogName(user.username)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        preference(THEME_PREF_KEY){
            val themeNames = resources.getStringArray(R.array.theme_names)
            lifecycleScope.launch {
                viewModel.getThemeNamePosition().collect {
                    summary = themeNames[it]
                }
            }
            onClick {
                navigateTo(R.id.selectThemeDialog)
            }
        }

        preference(BATTERY_PREF_KEY){
            setSummary(
                if (requireContext().isIgnoringBatteryOptimizations()) R.string.all_in_order
                else R.string.require_action
            )
            onClick {
                navigateTo(R.id.batteryOptimizationFragment)
            }
        }

        preference(LOG_OUT_PREF_KEY){
            isVisible = viewModel.isUserLoggedIn()
            onClick {
                lifecycleScope.launch {
                    when(viewModel.logOut()){
                        SimpleResult.Failure -> {
                            (requireParentFragment() as SettingsBottomSheet).showSnackBar(R.string.snack_general_error)
                        }
                        SimpleResult.Success -> {
                            val nav = NavOptions.Builder().setPopUpTo(R.id.navigation, true).build()
                            findNavController().navigate(R.id.loginFragment, null, nav)
                        }
                    }
                }
            }
        }

        preference(LOG_IN_PREF_KEY){
            isVisible = !viewModel.isUserLoggedIn()
            onClick {
                navigateTo(R.id.internalLoginFragment)
            }
        }

        preferenceOnClick(CREDITS_PREF_KEY){
            navigateTo(R.id.creditsBottomSheet)
        }

        preferenceOnClick(TICKET_PREF_KEY){
            navigateTo(R.id.ticketBottomSheet)
        }

        preference(VERSION_PREF_KEY){
            summary = BuildConfig.VERSION_NAME + " ( ${BuildConfig.VERSION_CODE} )"
            onClick {
                viewModel.setPandaAnimationPref(true)
                (requireParentFragment() as SettingsBottomSheet).showSnackBar(R.string.snack_panda_unlocked)
            }
        }

        preferenceOnClick(RATE_APP_PREF_KEY){
           try {
               launchWebBrowserIntent(getString(R.string.pref_play_store_uri))
           }catch (e:Exception){
               launchWebBrowserIntent(getString(R.string.pref_play_store_web_url))
           }
        }

        preferenceOnClick(LICENSES_PREF_KEY){
            Intent(requireContext(), OssLicensesMenuActivity::class.java).apply {
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licenses))
                startActivity(this)
            }
        }
    }
}