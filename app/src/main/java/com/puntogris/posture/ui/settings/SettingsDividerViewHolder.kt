package com.puntogris.posture.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.SettingsDividerVhBinding
import com.puntogris.posture.databinding.SettingsItemTextBinding
import com.puntogris.posture.model.SettingItem

class SettingsDividerViewHolder(private val binding: SettingsDividerVhBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(settingItem: SettingItem){
        binding.settingItem = settingItem
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): SettingsDividerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SettingsDividerVhBinding.inflate(layoutInflater, parent, false)
            return SettingsDividerViewHolder(binding)
        }
    }
}