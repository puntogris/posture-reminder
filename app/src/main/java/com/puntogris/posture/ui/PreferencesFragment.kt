package com.puntogris.posture.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.puntogris.posture.R
import com.puntogris.posture.TimePickerPreference
import com.puntogris.posture.TimePreferenceDialogFragmentCompat

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.mainFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
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

