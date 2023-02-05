package com.puntogris.posture.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentSettingsBinding
import com.puntogris.posture.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.utils.constants.Constants.EDIT_NAME_KEY
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPreferenceScreen()
        setFragmentResultsListener()
    }

    private fun createPreferenceScreen() {
        childFragmentManager
            .beginTransaction()
            .replace(binding.container.id, PreferencesFragment())
            .commit()
    }

    private fun setFragmentResultsListener() {
        setFragmentResultListener(EDIT_NAME_KEY) { _, bundle ->
            val editUsernameSuccessfully = bundle.getBoolean(DATA_KEY)
            val snackMessage = if (editUsernameSuccessfully) {
                R.string.snack_edit_username_success
            } else {
                R.string.snack_connection_error
            }
            showSnackBarAnchored(snackMessage)
        }
    }

    fun showSnackBarAnchored(message: Int, action: View.OnClickListener? = null) {
        Snackbar.make(binding.root, getText(message), Snackbar.LENGTH_LONG).apply {
            if (action != null) {
                setAction(R.string.action_login, action)
            }
        }.show()
    }
}
