package com.puntogris.posture.ui.main

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.model.DayLog

class DayHistoryMainPagerAdapter: RecyclerView.Adapter<DayHistoryMainViewHolder>(){

    private var items = listOf(DayLog())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHistoryMainViewHolder {
        return DayHistoryMainViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DayHistoryMainViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<DayLog>){
        if (list.isNotEmpty()) {
            items = list
            notifyDataSetChanged()
        }
    }
}