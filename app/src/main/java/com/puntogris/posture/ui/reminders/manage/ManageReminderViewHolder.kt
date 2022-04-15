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
        binding.reminderName.text = reminder.name
        binding.reminderColor.setReminderColor(reminder.color)
        binding.intervalSummary.text = reminder.timeIntervalSummary()
        binding.startSummary.setMinutesToHourlyTime(reminder.startTime)
        binding.endSummary.setMinutesToHourlyTime(reminder.endTime)
        binding.daysSummary.setDaysSummary(reminder)
        binding.selectReminder.setOnClickListener { selectReminder(reminder) }
        binding.editReminder.setOnClickListener { editReminder(reminder) }
    }

    companion object {
        fun from(parent: ViewGroup): ManageReminderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ManageReminderVhBinding.inflate(layoutInflater, parent, false)
            return ManageReminderViewHolder(binding)
        }
    }
}