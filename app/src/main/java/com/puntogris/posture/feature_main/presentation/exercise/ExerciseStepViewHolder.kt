package com.puntogris.posture.feature_main.presentation.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ExerciseStepVhBinding

class ExerciseStepViewHolder(private val binding: ExerciseStepVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(step: String, position: Int) {
        binding.step = step
        binding.position = position
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ExerciseStepViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ExerciseStepVhBinding.inflate(layoutInflater, parent, false)
            return ExerciseStepViewHolder(binding)
        }
    }
}