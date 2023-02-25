package com.puntogris.posture.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DayLogHomePagerItemBinding
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.utils.setDayMonth
import com.puntogris.posture.utils.setPagerDay
import com.puntogris.posture.utils.setText

class DayLogHomeViewHolder(private val binding: DayLogHomePagerItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(dayLog: DayLog) {
        with(binding) {
            setPagerDay(textViewDayLabel, adapterPosition)
            setDayMonth(textViewDayValue, adapterPosition)
            textViewNotificationValue.text = dayLog.notifications.toString()
            textViewExercisesValue.text = dayLog.exercises.toString()
            textViewExperienceValue.setText(R.string.number_plus_prefix, dayLog.expGained)
        }
    }

    companion object {
        fun from(parent: ViewGroup): DayLogHomeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DayLogHomePagerItemBinding.inflate(layoutInflater, parent, false)
            return DayLogHomeViewHolder(binding)
        }
    }
}