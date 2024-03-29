package com.puntogris.posture.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ExerciseItemVhBinding
import com.puntogris.posture.domain.model.Exercise
import com.puntogris.posture.utils.setBackgroundColorTintView
import com.puntogris.posture.utils.setExerciseDuration

class ExerciseViewHolder(private val binding: ExerciseItemVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(exercise: Exercise, clickListener: (Exercise) -> Unit) {
        with(binding) {
            textViewExerciseTitleLabel.setText(exercise.title)
            setExerciseDuration(textViewExerciseDuration, exercise.duration)
            textViewExerciseSummary.setText(exercise.summary)
            viewExerciseColor.setBackgroundColorTintView(exercise.color)
            root.setOnClickListener {
                clickListener(exercise)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): ExerciseViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ExerciseItemVhBinding.inflate(layoutInflater, parent, false)
            return ExerciseViewHolder(binding)
        }
    }
}
