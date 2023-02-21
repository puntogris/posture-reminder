package com.puntogris.posture.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.R
import com.puntogris.posture.utils.constants.Keys
import com.puntogris.posture.utils.extensions.launchWebBrowserIntent
import com.puntogris.posture.utils.extensions.onClick
import com.puntogris.posture.utils.extensions.preference
import com.puntogris.posture.utils.extensions.preferenceOnClick
import com.puntogris.posture.utils.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_preferences, rootKey)

        preference(Keys.VERSION_PREF_KEY) {
            summary = getString(
                R.string.pref_version_formatted,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE
            )
            onClick {
                viewModel.setPandaAnimationPref(true)
                showSnackBar(R.string.snack_panda_unlocked)
            }
        }
        preferenceOnClick(Keys.RATE_APP_PREF_KEY) {
            launchWebBrowserIntent(R.string.pref_play_store_web_url)
        }
        preferenceOnClick(Keys.LICENSES_PREF_KEY) {
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licenses))
            startActivity(
                Intent(requireContext(), OssLicensesMenuActivity::class.java)
            )
        }
    }
}
