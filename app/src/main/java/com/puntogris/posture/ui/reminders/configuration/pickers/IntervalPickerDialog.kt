package com.puntogris.posture.ui.reminders.configuration.pickers

import android.app.Dialog
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogIntervalPickerBinding
import com.puntogris.posture.utils.extensions.data
import com.puntogris.posture.utils.extensions.getHours
import com.puntogris.posture.utils.extensions.getMinutes
import com.puntogris.posture.utils.extensions.onPositive
import com.puntogris.posture.utils.viewBinding

class IntervalPickerDialog(
    private val currentInterval: Int,
    private val onPickedAction: (Int) -> Unit
) : DialogFragment() {

    private val binding by viewBinding(DialogIntervalPickerBinding::inflate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (currentInterval != 0) {
            binding.editTextIntervalHours.setText(currentInterval.getHours().toString())
            binding.editTextIntervalMinutes.setText(currentInterval.getMinutes().toString())
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
        val hours = binding.editTextIntervalHours.data.toIntOrNull() ?: 0
        val minutes = binding.editTextIntervalMinutes.data.toIntOrNull() ?: 0
        val interval = hours * 60 + minutes
        if (interval == 0) {
            binding.textViewIntervalAlert.isVisible = true
        } else {
            onPickedAction(interval)
            dismiss()
        }
    }
}
