package com.puntogris.posture.ui.reminders.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R
import com.puntogris.posture.databinding.ManageReminderVhBinding
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.SelectableReminder
import com.puntogris.posture.utils.setDaysSummary
import com.puntogris.posture.utils.setMinutesToHourlyTime
import com.puntogris.posture.utils.setReminderColor

class ManageReminderViewHolder(private val binding: ManageReminderVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(reminder: SelectableReminder, onSelected: (Reminder) -> Unit, isLastItem: Boolean) {

        with(binding) {
            textViewReminderNameValue.text = reminder.reminder.name
            viewReminderColor.setReminderColor(reminder.reminder.color)
            textViewReminderIntervalValue.text = reminder.reminder.timeIntervalSummary()
            setMinutesToHourlyTime(textViewReminderStartValue, reminder.reminder.startTime)
            setMinutesToHourlyTime(textViewReminderEndValue, reminder.reminder.endTime)
            setDaysSummary(textViewReminderDaysValue, reminder.reminder)
            buttonSelectReminder.setOnClickListener { onSelected(reminder.reminder) }
            buttonSelectReminder.isEnabled = !reminder.isSelected
            buttonSelectReminder.setText(
                if (reminder.isSelected) R.string.action_selected else R.string.action_select
            )
            viewDivider.isVisible = !isLastItem
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
