package com.puntogris.posture.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.DayLogHomePagerItemBinding
import com.puntogris.posture.model.DayLog

class DayLogHomeViewHolder(private val binding: DayLogHomePagerItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(dayLog: DayLog, position: Int) {
        binding.dayHistory = dayLog
        binding.position = position
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): DayLogHomeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DayLogHomePagerItemBinding.inflate(layoutInflater, parent, false)
            return DayLogHomeViewHolder(binding)
        }
    }
}