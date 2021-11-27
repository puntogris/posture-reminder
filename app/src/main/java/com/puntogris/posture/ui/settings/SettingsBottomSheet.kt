package com.puntogris.posture.ui.settings

import androidx.fragment.app.setFragmentResultListener
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetSettingsBinding
import com.puntogris.posture.ui.base.BaseBindingBottomSheetFragment
import com.puntogris.posture.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.utils.constants.Constants.EDIT_NAME_KEY
import com.puntogris.posture.utils.constants.Constants.SEND_TICKET_KEY
import com.puntogris.posture.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsBottomSheet : BaseBindingBottomSheetFragment<BottomSheetSettingsBinding>(
    R.layout.bottom_sheet_settings,
    true
) {

    override fun initializeViews() {
        binding.bottomSheet = this
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
        setFragmentResultListener(SEND_TICKET_KEY) { _, bundle ->
            val ticketSentSuccessfully = bundle.getBoolean(DATA_KEY)
            val snackMessage =
                if (ticketSentSuccessfully) R.string.snack_send_ticket_success
                else R.string.snack_connection_error
            showSnackBarAnchored(snackMessage)
        }
        setFragmentResultListener(EDIT_NAME_KEY) { _, bundle ->
            val editUsernameSuccessfully = bundle.getBoolean(DATA_KEY)
            val snackMessage =
                if (editUsernameSuccessfully) R.string.snack_edit_username_success
                else R.string.snack_connection_error
            showSnackBarAnchored(snackMessage)
        }
    }

    fun showSnackBarAnchored(message: Int){
        showSnackBar(message, anchorView = binding.guidelineEnd)
    }
}

