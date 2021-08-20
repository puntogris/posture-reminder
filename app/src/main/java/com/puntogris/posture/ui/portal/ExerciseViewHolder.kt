package com.puntogris.posture.ui.portal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ExerciseItemVhBinding
import com.puntogris.posture.model.Exercise

class ExerciseViewHolder(private val binding: ExerciseItemVhBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(exercise: Exercise, clickListener: (Exercise) -> Unit){
        binding.exercise = exercise
        binding.root.setOnClickListener {
            clickListener(exercise)
        }
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): ExerciseViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ExerciseItemVhBinding.inflate(layoutInflater, parent, false)
            return ExerciseViewHolder(binding)
        }
    }
}