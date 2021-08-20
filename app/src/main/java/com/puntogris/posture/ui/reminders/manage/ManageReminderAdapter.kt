package com.puntogris.posture.ui.reminders.manage

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.utils.SwipeToDeleteCallback

class ManageReminderAdapter(
    private val context: Context,
    private val selectReminder: (Reminder) -> Unit,
    private val editReminder: (Reminder) -> Unit,
    private val removeReminder: (Reminder) -> Unit
) : RecyclerView.Adapter<ManageReminderViewHolder>(
) {
    private var items = listOf<Reminder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ManageReminderViewHolder.from(parent)

    override fun onBindViewHolder(holder: ManageReminderViewHolder, position: Int) {
        holder.bind(items[position], selectReminder, editReminder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeReminder.invoke(items[viewHolder.adapterPosition])
            }
        }.apply { ItemTouchHelper(this).attachToRecyclerView(recyclerView) }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Reminder>) {
        items = list
        notifyDataSetChanged()
    }

}