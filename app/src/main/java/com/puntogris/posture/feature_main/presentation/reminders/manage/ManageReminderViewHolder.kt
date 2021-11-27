package com.puntogris.posture.feature_main.presentation.reminders.manage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ManageReminderVhBinding
import com.puntogris.posture.feature_main.domain.model.Reminder

class ManageReminderViewHolder(private val binding: ManageReminderVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        reminder: Reminder,
        selectReminder: (Reminder) -> Unit,
        editReminder: (Reminder) -> Unit
    ) {
        binding.reminder = reminder
        binding.selectReminder.setOnClickListener { selectReminder(reminder) }
        binding.editReminder.setOnClickListener { editReminder(reminder) }
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ManageReminderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ManageReminderVhBinding.inflate(layoutInflater, parent, false)
            return ManageReminderViewHolder(binding)
        }
    }
}