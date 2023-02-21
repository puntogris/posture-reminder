package com.puntogris.posture.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.puntogris.posture.R

class ContactPreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.contact_preferences, rootKey)
    }
}
