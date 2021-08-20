package com.puntogris.posture.ui.reminders.new_edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ReminderNameVhBinding
import com.puntogris.posture.model.ReminderUi

class ReminderNameViewHolder(private val binding: ReminderNameVhBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(reminderItem: ReminderUi.Name, clickListener: (ReminderUi)-> Unit){
        binding.item = reminderItem
        binding.itemEditText.addTextChangedListener {
            reminderItem.value = it.toString()
            clickListener(reminderItem)
        }
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): ReminderNameViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReminderNameVhBinding.inflate(layoutInflater, parent, false)
            return ReminderNameViewHolder(binding)
        }
    }
}