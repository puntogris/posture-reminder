package com.puntogris.posture.ui.reminders.new_edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ReminderColorVhBinding
import com.puntogris.posture.model.ReminderUi

class ReminderColorViewHolder(private val binding: ReminderColorVhBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(reminderItem: ReminderUi.Color, clickListener: (ReminderUi)-> Unit){
        binding.item = reminderItem
        binding.reminderColorImage.setOnClickListener { clickListener(reminderItem) }
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): ReminderColorViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReminderColorVhBinding.inflate(layoutInflater, parent, false)
            return ReminderColorViewHolder(binding)
        }
    }
}