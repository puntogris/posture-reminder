package com.puntogris.posture.ui.reminders.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R
import com.puntogris.posture.databinding.ManageReminderVhBinding
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.setDaysSummary
import com.puntogris.posture.utils.setMinutesToHourlyTime
import com.puntogris.posture.utils.setReminderColor

class ManageReminderViewHolder(private val binding: ManageReminderVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        reminder: SelectableReminder,
        selectListener: (Reminder) -> Unit,
        editListener: (Reminder) -> Unit,
        isLastItem: Boolean
    ) {
        with(binding) {
            reminderName.text = reminder.reminder.name
            reminderColor.setReminderColor(reminder.reminder.color)
            intervalSummary.text = reminder.reminder.timeIntervalSummary()
            startSummary.setMinutesToHourlyTime(reminder.reminder.startTime)
            endSummary.setMinutesToHourlyTime(reminder.reminder.endTime)
            daysSummary.setDaysSummary(reminder.reminder)
            editReminder.setOnClickListener { editListener(reminder.reminder) }
            selectReminder.setOnClickListener { selectListener(reminder.reminder) }
            selectReminder.isEnabled = !reminder.isSelected
            selectReminder.setText(
                if (reminder.isSelected) R.string.action_selected else R.string.action_select
            )
            divider.isVisible = !isLastItem
        }
    }

    companion object {
        fun from(parent: ViewGroup): ManageReminderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ManageReminderVhBinding.inflate(layoutInflater, parent, false)
            return ManageReminderViewHolder(binding)
        }
    }
}
