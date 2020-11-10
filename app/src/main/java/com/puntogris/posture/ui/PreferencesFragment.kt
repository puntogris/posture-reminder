package com.puntogris.posture.ui

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.puntogris.posture.R
import com.puntogris.posture.preferences.TimePickerPreference
import com.puntogris.posture.preferences.TimePreferenceDialogFragmentCompat

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is TimePickerPreference) {
            TimePreferenceDialogFragmentCompat.newInstance(preference.key).let {
                it.setTargetFragment(this, 0)
                it.show(parentFragmentManager, null)
            }
        } else super.onDisplayPreferenceDialog(preference)

    }
}

