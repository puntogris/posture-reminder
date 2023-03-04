package com.puntogris.posture.ui.reminders.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ReminderColorVhBinding
import com.puntogris.posture.utils.ReminderUi
import com.puntogris.posture.utils.setReminderColor

class ReminderColorViewHolder(private val binding: ReminderColorVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(reminderItem: ReminderUi.Color, clickListener: (ReminderUi) -> Unit) {
        binding.imageViewReminderColor.apply {
            setReminderColor(reminderItem.color)
            setOnClickListener { clickListener(reminderItem) }
        }
    }

    companion object {
        fun from(parent: ViewGroup): ReminderColorViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReminderColorVhBinding.inflate(layoutInflater, parent, false)
            return ReminderColorViewHolder(binding)
        }
    }
}
