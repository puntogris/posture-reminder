package com.puntogris.posture.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ExerciseStepVhBinding

class ExerciseStepViewHolder(private val binding: ExerciseStepVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(step: String, position: Int) {
        binding.stepDescription.text = "$position - $step"
    }

    companion object {
        fun from(parent: ViewGroup): ExerciseStepViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ExerciseStepVhBinding.inflate(layoutInflater)
            return ExerciseStepViewHolder(binding)
        }
    }
}