package com.puntogris.posture.ui.reminders.new_edit

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogReminderNamePickerBinding
import com.puntogris.posture.utils.extensions.data
import com.puntogris.posture.utils.extensions.onPositive
import com.puntogris.posture.utils.viewBinding

class ReminderNamePickerDialog(
    val name: String,
    private val result: (String) -> Unit
) : DialogFragment() {

    private val binding by viewBinding(DialogReminderNamePickerBinding::inflate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.name)
            .setView(binding.root)
            .setPositiveButton(R.string.action_done, null)
            .setNegativeButton(R.string.action_cancel, null)
            .create().also {
                it.onPositive(::onPositiveButtonClicked)
            }
    }

    private fun onPositiveButtonClicked() {
        result(binding.editTextDialogReminderName.data)
        dismiss()
    }
}