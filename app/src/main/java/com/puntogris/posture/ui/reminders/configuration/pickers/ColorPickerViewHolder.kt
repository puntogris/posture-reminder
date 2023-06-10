package com.puntogris.posture.ui.reminders.configuration.pickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.ColorPickerViewHolderBinding
import com.puntogris.posture.utils.setBackgroundColorTintView

class ColorPickerViewHolder(
    private val binding: ColorPickerViewHolderBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(color: Int, clickListener: (Int) -> Unit) {
        binding.viewColor.apply {
            setBackgroundColorTintView(color)
            setOnClickListener {
                clickListener(color)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): ColorPickerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ColorPickerViewHolderBinding.inflate(layoutInflater, parent, false)
            return ColorPickerViewHolder(binding)
        }
    }
}
