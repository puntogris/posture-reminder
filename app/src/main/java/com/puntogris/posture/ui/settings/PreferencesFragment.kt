package com.puntogris.posture.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.puntogris.posture.R
import com.puntogris.posture.utils.constants.Keys.ACCOUNT_PREFERENCES_KEY
import com.puntogris.posture.utils.constants.Keys.APP_PREFERENCES_KEY
import com.puntogris.posture.utils.constants.Keys.CONTACT_PREFERENCES_KEY
import com.puntogris.posture.utils.constants.Keys.HELP_PREFERENCES_KEY
import com.puntogris.posture.utils.extensions.preferenceOnClick

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        preferenceOnClick(ACCOUNT_PREFERENCES_KEY) {
            findNavController().navigate(R.id.accountPreferencesFragment)
        }
        preferenceOnClick(HELP_PREFERENCES_KEY) {
            findNavController().navigate(R.id.helpPreferencesFragment)
        }
        preferenceOnClick(CONTACT_PREFERENCES_KEY) {
            findNavController().navigate(R.id.contactPreferencesFragment)
        }
        preferenceOnClick(APP_PREFERENCES_KEY) {
            findNavController().navigate(R.id.appPreferencesFragment)
        }
    }
}