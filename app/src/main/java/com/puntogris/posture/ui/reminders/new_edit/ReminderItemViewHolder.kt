package com.puntogris.posture.ui.reminders.new_edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ReminderItemVhBinding
import com.puntogris.posture.utils.ReminderUi
import com.puntogris.posture.utils.gone

class ReminderItemViewHolder(private val binding: ReminderItemVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        reminderItem: ReminderUi.Item,
        clickListener: (ReminderUi) -> Unit,
        isLastItem: Boolean
    ) {
        binding.itemName.setText(reminderItem.title)
        binding.itemDescription.text = reminderItem.description
        binding.root.setOnClickListener { clickListener(reminderItem) }
        if (isLastItem) binding.divider.gone()
    }

    companion object {
        fun from(parent: ViewGroup): ReminderItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ReminderItemVhBinding.inflate(layoutInflater)
            return ReminderItemViewHolder(binding)
        }
    }
}