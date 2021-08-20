package com.puntogris.posture.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.DayHistoryMainPagerItemBinding
import com.puntogris.posture.model.DayLog

class DayHistoryMainViewHolder(private val binding: DayHistoryMainPagerItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(dayLog: DayLog, position: Int){
        binding.dayHistory = dayLog
        binding.position = position
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): DayHistoryMainViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DayHistoryMainPagerItemBinding.inflate(layoutInflater, parent, false)
            return DayHistoryMainViewHolder(binding)
        }
    }
}