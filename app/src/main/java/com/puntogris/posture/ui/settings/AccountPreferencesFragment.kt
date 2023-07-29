package com.puntogris.posture.ui.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.puntogris.posture.R
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Keys
import com.puntogris.posture.utils.extensions.isIgnoringBatteryOptimizations
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.onChange
import com.puntogris.posture.utils.extensions.onClick
import com.puntogris.posture.utils.extensions.preference
import com.puntogris.posture.utils.extensions.showSnackBar
import com.puntogris.posture.utils.extensions.switchPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey)
        setupThemePref()
        setupBatteryPref()
        setupLoginPref()
        setupDeletionPref()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserNamePref()
        setupSoundPrefs()
    }

    private fun setupSoundPrefs() {
        switchPreference(Keys.ENABLE_EXP_SOUND_KEY) {
            launchAndRepeatWithViewLifecycle {
                isChecked = viewModel.isExpSoundEnabled.first()
            }
            onChange(viewModel::setExpSoundPref)
        }
        switchPreference(Keys.ENABLE_EXERCISE_SOUND_KEY) {
            launchAndRepeatWithViewLifecycle {
                isChecked = viewModel.isExerciseSoundEnabled.first()
            }
            onChange(viewModel::setExerciseSoundPref)
        }
    }

    private fun setupDeletionPref() {
        preference(Keys.DELETE_ACCOUNT_PREF_KEY) {
            isVisible = viewModel.isUserLoggedIn()
            onClick {
                findNavController().navigate(R.id.deleteAccountFragment)
            }
        }
    }

    private fun setupLoginPref() {
        preference(Keys.LOG_OUT_PREF_KEY) {
            isVisible = viewModel.isUserLoggedIn()
            onClick {
                lifecycleScope.launch {
                    when (viewModel.logOut()) {
                        SimpleResult.Failure -> showSnackBar(R.string.snack_general_error)
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

    private fun setupThemePref() {
        preference(Keys.THEME_PREF_KEY) {
            val themeNames = resources.getStringArray(R.array.theme_names)
            lifecycleScope.launch {
                viewModel.appTheme.collect {
                    summary = themeNames[it]
                }
            }
            onClick {
                lifecycleScope.launch {
                    SelectThemeDialog(
                        currentTheme = viewModel.getAppTheme(),
                        onPositiveAction = ::updateTheme
                    ).show(parentFragmentManager, "SELECT_THEME_DIALOG")
                }
            }
        }
    }

    private fun setupBatteryPref() {
        preference(Keys.BATTERY_PREF_KEY) {
            setSummary(
                if (requireContext().isIgnoringBatteryOptimizations()) {
                    R.string.optimization_correct
                } else {
                    R.string.optimization_require_action
                }
            )
            onClick {
                if (requireContext().isIgnoringBatteryOptimizations()) {
                    showSnackBar(R.string.optimization_correct)
                } else {
                    findNavController().navigate(R.id.batteryOptimizationFragment)
                }
            }
        }
    }

    private fun setupUserNamePref() {
        preference(Keys.USERNAME_PREF_KEY) {
            launchAndRepeatWithViewLifecycle {
                viewModel.user.collectLatest {
                    summary = it.username.ifBlank { getString(R.string.human) }
                }
            }
            onClick {
                if (viewModel.isUserLoggedIn()) {
                    EditUserNameDialog(
                        currentName = viewModel.user.value.username,
                        onPositiveAction = ::updateUserName
                    )
                } else {
                    showSnackBar(
                        message = R.string.snack_action_requires_login,
                        actionRes = R.string.action_login
                    ) {
                        findNavController().navigate(R.id.internalLoginFragment)
                    }
                }
            }
        }
    }

    private fun updateUserName(name: String) {
        lifecycleScope.launch {
            val snackMessage = when (viewModel.updateUserName(name)) {
                SimpleResult.Failure -> R.string.snack_connection_error
                SimpleResult.Success -> R.string.snack_edit_username_success
            }
            showSnackBar(snackMessage)
        }
    }

    private fun updateTheme(value: Int) {
        val themeValuesArray = resources.getIntArray(R.array.theme_values)
        themeValuesArray[value].let {
            lifecycleScope.launch {
                viewModel.updateTheme(it)
                AppCompatDelegate.setDefaultNightMode(it)
            }
        }
    }
}
