package com.puntogris.posture.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentSettingsBinding
import com.puntogris.posture.utils.viewBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPreferenceScreen()
    }

    private fun createPreferenceScreen() {
        childFragmentManager
            .beginTransaction()
            .replace(binding.container.id, PreferencesFragment())
            .commit()
    }
}
