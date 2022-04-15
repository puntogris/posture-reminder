package com.puntogris.posture.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetSettingsBinding
import com.puntogris.posture.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.utils.constants.Constants.EDIT_NAME_KEY
import com.puntogris.posture.utils.constants.Constants.SEND_TICKET_KEY
import com.puntogris.posture.utils.extensions.setupAsFullScreen
import com.puntogris.posture.utils.extensions.showSnackBar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(BottomSheetSettingsBinding::bind)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setupAsFullScreen(isDraggable = true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPreferenceScreen()
        setFragmentResultsListener()
        binding.closeButton.setOnClickListener {
            dismiss()
        }
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

    fun showSnackBarAnchored(
        message: Int,
        actionText: Int = R.string.action_undo,
        actionListener: View.OnClickListener? = null
    ) {
        showSnackBar(
            message,
            anchorView = binding.guidelineEnd,
            actionText = actionText,
            actionListener = actionListener
        )
    }
}

