package com.puntogris.posture.ui.exercise

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ExerciseStepsAdapter(private val steps: Array<String>) :
    RecyclerView.Adapter<ExerciseStepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseStepViewHolder {
        return ExerciseStepViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExerciseStepViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount() = steps.size
}