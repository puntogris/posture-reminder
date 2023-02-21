package com.puntogris.posture.ui.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.WelcomeItemVhBinding
import com.puntogris.posture.utils.WelcomeItem

class WelcomeItemViewHolder(private val binding: WelcomeItemVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(welcomeItem: WelcomeItem) {
        binding.imageViewWelcomeItemIllustration.setImageResource(welcomeItem.image)
        binding.textViewWelcomeItemTitle.setText(welcomeItem.tittle)
    }

    companion object {
        fun from(parent: ViewGroup): WelcomeItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = WelcomeItemVhBinding.inflate(layoutInflater, parent, false)
            return WelcomeItemViewHolder(binding)
        }
    }
}
