package com.puntogris.posture.ui.exercises

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ExerciseStepVhBinding

class ExerciseStepViewHolder(private val binding: ExerciseStepVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(step: String) {
        binding.stepDescription.text = SpannableStringBuilder().apply {
            append(adapterPosition.inc().toString())
            setSpan(StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            append(" - ")
            append(step)
        }
    }

    companion object {
        fun from(parent: ViewGroup): ExerciseStepViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ExerciseStepVhBinding.inflate(layoutInflater, parent, false)
            return ExerciseStepViewHolder(binding)
        }
    }
}
