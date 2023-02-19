package com.puntogris.posture.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.puntogris.posture.R
import com.puntogris.posture.utils.constants.Keys
import com.puntogris.posture.utils.extensions.preferenceOnClick

class HelpPreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.help_preferences, rootKey)

        preferenceOnClick(Keys.TICKET_PREF_KEY) {
            findNavController().navigate(R.id.ticketFragment)
        }
    }
}