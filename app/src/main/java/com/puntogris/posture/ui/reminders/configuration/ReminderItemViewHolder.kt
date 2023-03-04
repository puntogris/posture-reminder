package com.puntogris.posture.ui.reminders.configuration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ReminderItemVhBinding
import com.puntogris.posture.utils.ReminderUi

class ReminderItemViewHolder(private val binding: ReminderItemVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        reminderItem: ReminderUi.Item,
        clickListener: (ReminderUi) -> Unit,
        isLastItem: Boolean
    ) {
        binding.textViewItemName.setText(reminderItem.title)
        binding.textViewItemDescription.text = reminderItem.description
        binding.root.setOnClickListener { clickListener(reminderItem) }
        binding.viewDivider.isVisible = !isLastItem
    }

    companion object {
        fun from(parent: ViewGroup): ReminderItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReminderItemVhBinding.inflate(layoutInflater, parent, false)
            return ReminderItemViewHolder(binding)
        }
    }
}
