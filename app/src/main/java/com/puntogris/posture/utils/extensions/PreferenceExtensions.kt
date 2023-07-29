package com.puntogris.posture.utils.extensions

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

inline fun PreferenceFragmentCompat.preference(key: String, block: Preference.() -> Unit) {
    findPreference<Preference>(key)?.apply {
        block(this)
    }
}

inline fun PreferenceFragmentCompat.switchPreference(key: String, block: SwitchPreference.() -> Unit) {
    findPreference<SwitchPreference>(key)?.apply {
        block(this)
    }
}

inline fun Preference.onClick(crossinline block: () -> Unit) {
    setOnPreferenceClickListener {
        block()
        true
    }
}

inline fun SwitchPreference.onChange(crossinline block: (Boolean) -> Unit) {
    setOnPreferenceChangeListener { _, value ->
        if (value is Boolean) {
            block(value)
        }
        true
    }
}

inline fun PreferenceFragmentCompat.preferenceOnClick(key: String, crossinline block: () -> Unit) {
    findPreference<Preference>(key)?.setOnPreferenceClickListener {
        block()
        true
    }
}
