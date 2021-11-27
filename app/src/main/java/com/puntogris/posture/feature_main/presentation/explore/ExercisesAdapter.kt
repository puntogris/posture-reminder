package com.puntogris.posture.feature_main.presentation.explore

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.feature_main.data.datasource.local.LocalDataSource
import com.puntogris.posture.feature_main.domain.model.Exercise

class ExercisesAdapter(private val clickListener: (Exercise) -> Unit) :
    RecyclerView.Adapter<ExerciseViewHolder>() {

    private var items = LocalDataSource.exercisesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
    }

    override fun getItemCount() = items.size
}