package com.puntogris.posture.ui.reminders.new_edit

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R

class DaysPickerDialog(
    private val initialDays: List<Int>,
    private val onSaveAction: (List<Int>) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val saveDays = initialDays.toMutableList()
        val days = resources.getStringArray(R.array.alarmDays)
        val checkedItems = BooleanArray(days.size, initialDays::contains)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.alarm_days_title)
            .setMultiChoiceItems(days, checkedItems) { _, option, isChecked ->
                if (isChecked) {
                    saveDays.add(option)
                } else {
                    saveDays.remove(option)
                }
            }
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSaveAction(saveDays)
            }
            .setNegativeButton(R.string.action_cancel, null)
            .create()
    }
}
