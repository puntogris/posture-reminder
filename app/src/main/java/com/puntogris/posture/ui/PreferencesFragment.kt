package com.puntogris.posture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.preference.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.R
import com.puntogris.posture.preferences.ReportBugDialog
import com.puntogris.posture.preferences.TimePickerPreference
import com.puntogris.posture.preferences.TimePreferenceDialogFragmentCompat

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>("panda_animation")?.setOnPreferenceClickListener {preference ->
            preference.sharedPreferences.edit().putBoolean(preference.key, true).apply()
            true
        }

        findPreference<Preference>("bug_report")?.setOnPreferenceClickListener {
            ReportBugDialog().show(parentFragmentManager, "dialog")
            true
        }

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
