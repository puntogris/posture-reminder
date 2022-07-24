package com.puntogris.posture.ui.reminders.manage

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.SelectableReminder
import com.puntogris.posture.utils.SwipeToDeleteCallback

class ManageReminderAdapter(
    private val context: Context,
    private val selectListener: (Reminder) -> Unit,
    private val editListener: (Reminder) -> Unit,
    private val deleteListener: (Reminder) -> Unit
) : ListAdapter< SelectableReminder, ManageReminderViewHolder>(SelectableReminderComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ManageReminderViewHolder.from(parent)

    override fun onBindViewHolder(holder: ManageReminderViewHolder, position: Int) {
        holder.bind(getItem(position), selectListener, itemCount.dec() == position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        object : SwipeToDeleteCallback(context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = getItem(viewHolder.adapterPosition).reminder
                if (direction == ItemTouchHelper.RIGHT) {
                    deleteListener(item)
                } else if (direction == ItemTouchHelper.LEFT) {
                    editListener(item)
                }
            }
        }.apply { ItemTouchHelper(this).attachToRecyclerView(recyclerView) }
    }
}
