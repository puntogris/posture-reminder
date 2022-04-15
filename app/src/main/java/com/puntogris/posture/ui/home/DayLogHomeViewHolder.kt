package com.puntogris.posture.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.DayLogHomePagerItemBinding
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.utils.setDayMonth
import com.puntogris.posture.utils.setPagerDay

class DayLogHomeViewHolder(private val binding: DayLogHomePagerItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(dayLog: DayLog, position: Int) {
        binding.dayLabel.setPagerDay(position)
        binding.logDay.setDayMonth(position)
        binding.logNotifications.text = dayLog.notifications.toString()
        binding.logExercises.text = dayLog.exercises.toString()
        binding.logExperience.text = "+${dayLog.expGained}"
    }

    companion object {
        fun from(parent: ViewGroup): DayLogHomeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DayLogHomePagerItemBinding.inflate(layoutInflater)
            return DayLogHomeViewHolder(binding)
        }
    }
}