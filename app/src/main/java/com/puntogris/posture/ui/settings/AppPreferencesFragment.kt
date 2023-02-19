package com.puntogris.posture.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.R
import com.puntogris.posture.utils.constants.Keys
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.launchWebBrowserIntent
import com.puntogris.posture.utils.extensions.onClick
import com.puntogris.posture.utils.extensions.preference
import com.puntogris.posture.utils.extensions.preferenceOnClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AppPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey)

        preference(Keys.VERSION_PREF_KEY) {
            summary = BuildConfig.VERSION_NAME + " ( ${BuildConfig.VERSION_CODE} )"
            onClick {
                viewModel.setPandaAnimationPref(true)
                (requireParentFragment() as SettingsFragment).showSnackBarAnchored(R.string.snack_panda_unlocked)
            }
        }

        preferenceOnClick(Keys.CREDITS_PREF_KEY) {
            findNavController().navigate(R.id.creditsBottomSheet)
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