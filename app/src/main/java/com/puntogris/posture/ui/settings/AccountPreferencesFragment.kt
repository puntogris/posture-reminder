package com.puntogris.posture.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Keys
import com.puntogris.posture.utils.extensions.isIgnoringBatteryOptimizations
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.onClick
import com.puntogris.posture.utils.extensions.preference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountPreferencesFragment: PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey)

        preference(Keys.THEME_PREF_KEY) {
            val themeNames = resources.getStringArray(R.array.theme_names)
            lifecycleScope.launch {
                viewModel.appTheme.collect {
                    summary = themeNames[it]
                }
            }
            onClick {
                findNavController().navigate(R.id.selectThemeDialog)
            }
        }

        preference(Keys.BATTERY_PREF_KEY) {
            setSummary(
                if (requireContext().isIgnoringBatteryOptimizations()) R.string.optimization_correct
                else R.string.optimization_require_action
            )
            onClick {
                findNavController().navigate(R.id.batteryOptimizationFragment)
            }
        }

        preference(Keys.DELETE_ACCOUNT_PREF_KEY) {
            isVisible = viewModel.isUserLoggedIn()
            onClick {
                findNavController().navigate(R.id.deleteAccountFragment)
            }
        }

        preference(Keys.LOG_OUT_PREF_KEY) {
            isVisible = viewModel.isUserLoggedIn()
            onClick {
                lifecycleScope.launch {
                    when (viewModel.logOut()) {
                        SimpleResult.Failure -> {
                            (requireParentFragment() as SettingsFragment).showSnackBarAnchored(
                                R.string.snack_general_error
                            )
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
                findNavController().navigate(R.id.internalLoginFragment)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preference(Keys.USERNAME_PREF_KEY) {
            launchAndRepeatWithViewLifecycle {
                viewModel.user.collectLatest {
                    summary = it.username.ifBlank { getString(R.string.human) }
                }
            }
            onClick {
                if (viewModel.isUserLoggedIn()) {
                    val action = SettingsFragmentDirections.actionSettingsToDialogName(
                        viewModel.user.value.username
                    )
                    findNavController().navigate(action)
                } else {
                    showRequireLoginSnack()
                }
            }
        }
    }

    private fun showRequireLoginSnack() {
        Snackbar.make(requireView(), R.string.snack_action_requires_login, Snackbar.LENGTH_LONG)
            .apply {
                setAction(R.string.action_login) {
                    findNavController().navigate(R.id.internalLoginFragment)
                }
            }.show()
    }
}