package com.puntogris.posture.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.puntogris.posture.R
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.utils.isIgnoringBatteryOptimizations
import com.puntogris.posture.utils.preference
import com.puntogris.posture.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.editUserNameDialog)
                true
            }
        }

        preference("theme_preference_key"){
            val themeNames = resources.getStringArray(R.array.theme_names)
            lifecycleScope.launch {
                viewModel.getThemeNamePosition().collect {
                    summary = themeNames[it]
                }
            }
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.selectThemeDialog)
                true
            }
        }

        preference("battery_preference_key"){
            setSummary(
                if (isIgnoringBatteryOptimizations()) R.string.all_in_order
                else R.string.require_action
            )
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.batteryOptimizationFragment)
                true
            }
        }
        preference("log_out_preference_key"){
            setOnPreferenceClickListener {
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
                true
            }
        }

        preference("credits_preference_key"){
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.creditsBottomSheet)
                true
            }
        }

        preference("ticket_preference_key"){
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.ticketBottomSheet)
                true
            }
        }

        preference("version_preference_key"){
            viewModel.setPandaAnimationPref(true)
            //showSnackBar(R.string.snack_panda_unlocked)
        }
    }
}