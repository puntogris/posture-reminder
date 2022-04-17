package com.puntogris.posture.ui.reminders.new_edit

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogIntervalPickerBinding
import com.puntogris.posture.utils.extensions.*
import com.puntogris.posture.utils.viewBinding

class IntervalPickerDialog(
    private val interval: Int,
    private val result: (Int?) -> Unit
) : DialogFragment() {

    private val binding by viewBinding(DialogIntervalPickerBinding::inflate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (interval != 0) {
            binding.intervalHours.setText(interval.getHours().toString())
            binding.intervalMinutes.setText(interval.getMinutes().toString())
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.time_interval_title)
            .setView(binding.root)
            .setPositiveButton(R.string.action_done, null)
            .setNegativeButton(R.string.action_cancel, null)
            .create().also {
                it.onPositive(::onPositiveButtonClicked)
            }
    }

    private fun onPositiveButtonClicked() {
        val hours = binding.intervalHours.data.toIntOrNull() ?: 0
        val minutes = binding.intervalMinutes.data.toIntOrNull() ?: 0
        val interval = hours * 60 + minutes
        if (interval == 0) {
            binding.intervalAlert.visible()
        } else {
            result(interval)
            dismiss()
        }
    }
}