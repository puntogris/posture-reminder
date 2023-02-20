package com.puntogris.posture.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.puntogris.posture.R
import com.puntogris.posture.utils.constants.Keys
import com.puntogris.posture.utils.extensions.preferenceOnClick
import com.puntogris.posture.utils.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.help_preferences, rootKey)

        preferenceOnClick(Keys.TICKET_PREF_KEY) {
            if (viewModel.isUserLoggedIn()) {
                findNavController().navigate(R.id.ticketFragment)

            } else {
                showSnackBar(
                    message = R.string.snack_action_requires_login,
                    actionRes = R.string.action_login
                ) {
                    findNavController().navigate(R.id.internalLoginFragment)
                }
            }
        }
    }
}