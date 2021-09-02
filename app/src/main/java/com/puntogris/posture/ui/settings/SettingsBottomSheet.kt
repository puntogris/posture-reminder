package com.puntogris.posture.ui.settings

import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetSettingsBinding
import com.puntogris.posture.ui.base.BaseBottomSheetFragment
import com.puntogris.posture.utils.Constants
import com.puntogris.posture.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsBottomSheet : BaseBottomSheetFragment<BottomSheetSettingsBinding>(R.layout.bottom_sheet_settings, true) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun initializeViews() {
        binding.bottomSheet = this
        createPreferenceScreen()
        setFragmentResultsListener()
    }

    private fun createPreferenceScreen(){
        childFragmentManager
            .beginTransaction()
            .replace(binding.container.id, PreferencesFragment())
            .commit()

    }

    private fun setFragmentResultsListener(){
        setFragmentResultListener(Constants.SEND_TICKET_KEY){ _, bundle ->
            val ticketSentSuccessfully = bundle.getBoolean(Constants.DATA_KEY)
            val snackMessage =
                if (ticketSentSuccessfully) R.string.snack_send_ticket_success
                else R.string.snack_connection_error
             showSnackBar(snackMessage)
        }
        setFragmentResultListener(Constants.EDIT_NAME_KEY){ _, bundle ->
            val editUsernameSuccessfully = bundle.getBoolean(Constants.DATA_KEY)
            val snackMessage =
                if (editUsernameSuccessfully) R.string.snack_edit_username_success
                else R.string.snack_connection_error
             showSnackBar(snackMessage)
        }
    }
}

