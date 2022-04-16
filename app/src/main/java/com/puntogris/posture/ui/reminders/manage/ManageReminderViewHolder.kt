package com.puntogris.posture.ui.reminders.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ManageReminderVhBinding
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.setDaysSummary
import com.puntogris.posture.utils.setMinutesToHourlyTime
import com.puntogris.posture.utils.setReminderColor

class ManageReminderViewHolder(private val binding: ManageReminderVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        reminder: Reminder,
        selectReminder: (Reminder) -> Unit,
        editReminder: (Reminder) -> Unit
    ) {
        with(binding) {
            reminderName.text = reminder.name
            reminderColor.setReminderColor(reminder.color)
            intervalSummary.text = reminder.timeIntervalSummary()
            startSummary.setMinutesToHourlyTime(reminder.startTime)
            endSummary.setMinutesToHourlyTime(reminder.endTime)
            daysSummary.setDaysSummary(reminder)
            this.selectReminder.setOnClickListener { selectReminder(reminder) }
            this.editReminder.setOnClickListener { editReminder(reminder) }
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