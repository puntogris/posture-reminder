package com.puntogris.posture.ui.reminders.new_edit

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.LocalDataSource
import com.puntogris.posture.model.ItemData
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.utils.ReminderUi
import com.puntogris.posture.utils.Utils.minutesFromMidnightToHourlyTime

class ReminderItemAdapter(private val context: Context, private val clickListener: (ReminderUi)-> Unit):  RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val items = listOf(
        ReminderUi.Color(R.color.grey),
        ReminderUi.Name(),
        ReminderUi.Item.Interval(ItemData(R.string.interval_required, context.getString(R.string.create_reminder_interval_summary))),
        ReminderUi.Item.Days(ItemData(R.string.days_required, context.getString(R.string.create_reminder_days_summary))),
        ReminderUi.Item.Start(ItemData(R.string.start_time_required, context.getString(R.string.create_reminder_start_summary))),
        ReminderUi.Item.End(ItemData(R.string.end_time_required, context.getString(R.string.create_reminder_end_summary))),
        ReminderUi.Item.Sound(ItemData(R.string.sound, context.getString(R.string.disabled))),
        ReminderUi.Item.Vibration(ItemData(R.string.vibration, context.getString(R.string.disabled)))
    )

    private val days = context.resources.getStringArray(R.array.alarmDays)
    private val vibrationPatterns = LocalDataSource().vibrationPatterns.map { it.title }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> ReminderColorViewHolder.from(parent)
            1 -> ReminderNameViewHolder.from(parent)
            else -> ReminderItemViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val isLastItem = itemCount -1 == position
        when(val item = items[position]){
            is ReminderUi.Color -> (holder as ReminderColorViewHolder).bind(item, clickListener)
            is ReminderUi.Name -> (holder as ReminderNameViewHolder).bind(item, clickListener)
            is ReminderUi.Item -> (holder as ReminderItemViewHolder).bind(item, clickListener, isLastItem)
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is ReminderUi.Color -> 0
            is ReminderUi.Name -> 1
            is ReminderUi.Item -> 2
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateConfigData(reminder: Reminder){
        (items[0] as ReminderUi.Color).color = reminder.color
        if (reminder.name.isNotBlank()) (items[1] as ReminderUi.Name).value = reminder.name
        if (reminder.timeInterval != 0) (items[2] as ReminderUi.Item).itemData.description = reminder.timeIntervalSummary()
        if (reminder.alarmDays.isNotEmpty()) (items[3] as ReminderUi.Item).itemData.description = reminder.alarmDaysSummary(days)
        if (reminder.startTime != -1) (items[4] as ReminderUi.Item).itemData.description = minutesFromMidnightToHourlyTime(reminder.startTime)
        if (reminder.endTime != -1) (items[5] as ReminderUi.Item).itemData.description = minutesFromMidnightToHourlyTime(reminder.endTime)
        (items[6] as ReminderUi.Item).itemData.description = if (reminder.soundName.isBlank()) context.getString(R.string.disabled) else reminder.soundName
        (items[7] as ReminderUi.Item).itemData.description = context.getString(vibrationPatterns[reminder.vibrationPattern])
        notifyDataSetChanged()
    }
}