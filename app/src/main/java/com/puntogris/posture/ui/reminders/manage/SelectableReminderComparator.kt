package com.puntogris.posture.ui.reminders.manage

import androidx.recyclerview.widget.DiffUtil
import com.puntogris.posture.domain.model.SelectableReminder

class SelectableReminderComparator: DiffUtil.ItemCallback<SelectableReminder>(){
    override fun areItemsTheSame(oldItem: SelectableReminder, newItem: SelectableReminder): Boolean {
        return oldItem.reminder.reminderId == newItem.reminder.reminderId
    }

    override fun areContentsTheSame(oldItem: SelectableReminder, newItem: SelectableReminder): Boolean {
        return oldItem == newItem
    }
}
