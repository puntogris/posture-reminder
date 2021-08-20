package com.puntogris.posture.ui.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.WelcomeItemVhBinding
import com.puntogris.posture.model.WelcomeItem

class WelcomeItemViewHolder(private val binding: WelcomeItemVhBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(welcomeItem: WelcomeItem){
        binding.welcomeItem = welcomeItem
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): WelcomeItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = WelcomeItemVhBinding.inflate(layoutInflater, parent, false)
            return WelcomeItemViewHolder(binding)
        }
    }
}