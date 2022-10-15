package com.puntogris.posture.ui.home

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.domain.model.DayLog

class DayLogHomeAdapter : RecyclerView.Adapter<DayLogHomeViewHolder>() {

    private var items = listOf(DayLog(), DayLog())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayLogHomeViewHolder {
        return DayLogHomeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DayLogHomeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<DayLog>) {
        if (list.isNotEmpty()) {
            if (list.size == 1) items = listOf(list.first(), DayLog())
            else if (list.size == 2) items = list
            notifyDataSetChanged()
        }
    }
}