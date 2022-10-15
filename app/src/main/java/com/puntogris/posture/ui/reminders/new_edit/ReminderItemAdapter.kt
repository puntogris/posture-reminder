package com.puntogris.posture.ui.reminders.new_edit

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.ReminderUi

class ReminderItemAdapter(
    private val context: Context,
    private val clickListener: (ReminderUi) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = listOf(
        ReminderUi.Color(),
        ReminderUi.Item.Name(
            description = context.getString(R.string.create_reminder_name_summary)
        ),
        ReminderUi.Item.Interval(
            description = context.getString(R.string.create_reminder_interval_summary)
        ),
        ReminderUi.Item.Days(
            description = context.getString(R.string.create_reminder_days_summary)
        ),
        ReminderUi.Item.Start(
            description = context.getString(R.string.create_reminder_start_summary)
        ),
        ReminderUi.Item.End(
            description = context.getString(R.string.create_reminder_end_summary)
        ),
        ReminderUi.Item.Sound(
            description = context.getString(R.string.disabled)
        ),
        ReminderUi.Item.Vibration(
            description = context.getString(R.string.disabled)
        )
    )

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.reminder_color_vh -> ReminderColorViewHolder.from(parent)
            else -> ReminderItemViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val isLastItem = itemCount - 1 == position
        when (val item = items[position]) {
            is ReminderUi.Color -> (holder as ReminderColorViewHolder).bind(item, clickListener)
            is ReminderUi.Item -> (holder as ReminderItemViewHolder).bind(
                item,
                clickListener,
                isLastItem
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ReminderUi.Color -> R.layout.reminder_color_vh
            is ReminderUi.Item -> R.layout.reminder_item_vh
        }
    }

    fun updateConfigData(reminder: Reminder) {
        items.forEachIndexed { index, reminderUi ->
            val wasUpdated = reminderUi.update(reminder, context)
            if (wasUpdated) notifyItemChanged(index)
        }
    }
}
