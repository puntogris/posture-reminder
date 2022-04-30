package com.puntogris.posture.ui.reminders.manage

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.SwipeToDeleteCallback

class ManageReminderAdapter(
    private val context: Context,
    private val selectListener: (Reminder) -> Unit,
    private val editListener: (Reminder) -> Unit,
    private val deleteListener: (Reminder) -> Unit
) : RecyclerView.Adapter<ManageReminderViewHolder>(
) {
    private var items = listOf<SelectableReminder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ManageReminderViewHolder.from(parent)

    override fun onBindViewHolder(holder: ManageReminderViewHolder, position: Int) {
        holder.bind(items[position], selectListener, editListener, items.size.dec() == position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteListener(items[viewHolder.adapterPosition].reminder)
            }
        }.apply { ItemTouchHelper(this).attachToRecyclerView(recyclerView) }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<SelectableReminder>) {
        items = list
        notifyDataSetChanged()
    }
}
