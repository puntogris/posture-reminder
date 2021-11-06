package com.puntogris.posture.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.R
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.constants.Keys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        preference(Keys.THEME_PREF_KEY) {
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

        preference(Keys.BATTERY_PREF_KEY) {
            setSummary(
                if (requireContext().isIgnoringBatteryOptimizations()) R.string.all_in_order
                else R.string.require_action
            )
            onClick {
                navigateTo(R.id.batteryOptimizationFragment)
            }
        }

        preference(Keys.DELETE_ACCOUNT_PREF_KEY) {
            isVisible = viewModel.isUserLoggedIn()
            onClick {
                navigateTo(R.id.deleteAccountFragment)
            }
        }

        preference(Keys.LOG_OUT_PREF_KEY) {
            isVisible = viewModel.isUserLoggedIn()
            onClick {
                lifecycleScope.launch {
                    when (viewModel.logOut()) {
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

        preference(Keys.LOG_IN_PREF_KEY) {
            isVisible = !viewModel.isUserLoggedIn()
            onClick {
                navigateTo(R.id.internalLoginFragment)
            }
        }

        preferenceOnClick(Keys.CREDITS_PREF_KEY) {
            navigateTo(R.id.creditsBottomSheet)
        }

        preferenceOnClick(Keys.TICKET_PREF_KEY) {
            if (viewModel.isUserLoggedIn()) {
                navigateTo(R.id.ticketBottomSheet)
            } else {
                showRequireLoginSnack()
            }
        }

        preference(Keys.VERSION_PREF_KEY) {
            summary = BuildConfig.VERSION_NAME + " ( ${BuildConfig.VERSION_CODE} )"
            onClick {
                viewModel.setPandaAnimationPref(true)
                (requireParentFragment() as SettingsBottomSheet).showSnackBar(R.string.snack_panda_unlocked)
            }
        }

        preferenceOnClick(Keys.RATE_APP_PREF_KEY) {
            launchWebBrowserIntent(getString(R.string.pref_play_store_web_url))
        }

        preferenceOnClick(Keys.LICENSES_PREF_KEY) {
            Intent(requireContext(), OssLicensesMenuActivity::class.java).apply {
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licenses))
                startActivity(this)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preference(Keys.USERNAME_PREF_KEY) {
            lifecycleScope.launch {
                viewModel.user.observe(viewLifecycleOwner) {
                    summary = it.username
                }
            }
            onClick {
                lifecycleScope.launch {
                    if (viewModel.isUserLoggedIn()) {
                        val action = SettingsBottomSheetDirections
                            .actionSettingsBottomSheetToDialogName(viewModel.user.value!!.username)
                        findNavController().navigate(action)
                    } else showRequireLoginSnack()
                }
            }
        }
    }

    private fun showRequireLoginSnack() {
        (requireParentFragment() as SettingsBottomSheet)
            .showSnackBar(
                message = R.string.snack_action_requires_login,
                actionText = R.string.action_login
            ) {
                navigateTo(R.id.internalLoginFragment)
            }
    }
}