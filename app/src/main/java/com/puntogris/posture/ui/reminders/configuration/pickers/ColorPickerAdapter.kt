package com.puntogris.posture.ui.reminders.configuration.pickers

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R

class ColorPickerAdapter(
    private val clickListener: (Int) -> Unit
) : RecyclerView.Adapter<ColorPickerViewHolder>() {

    private val colors = listOf(
        R.color.emerald_100, R.color.emerald_300, R.color.emerald_500, R.color.emerald_700, R.color.emerald_900,
        R.color.green_900, R.color.green_700, R.color.green_500, R.color.green_300, R.color.green_100,
        R.color.sky_100, R.color.sky_300, R.color.sky_500, R.color.sky_700, R.color.sky_900,
        R.color.indigo_900, R.color.indigo_700, R.color.indigo_500, R.color.indigo_300, R.color.indigo_100,
        R.color.rose_100, R.color.rose_300, R.color.rose_500, R.color.rose_700, R.color.rose_900,
        R.color.red_900, R.color.red_700, R.color.red_500, R.color.red_300, R.color.red_100,
        R.color.amber_100, R.color.amber_300, R.color.amber_500, R.color.amber_700, R.color.amber_900,
    )

    override fun getItemCount(): Int = colors.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorPickerViewHolder {
        return ColorPickerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ColorPickerViewHolder, position: Int) {
        holder.bind(colors[position], clickListener)
    }
}
