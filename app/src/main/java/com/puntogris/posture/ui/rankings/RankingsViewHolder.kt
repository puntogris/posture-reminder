package com.puntogris.posture.ui.rankings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.RankingProfileVhBinding
import com.puntogris.posture.model.UserPublicProfile
import com.puntogris.posture.utils.gone

class RankingsViewHolder(private val binding: RankingProfileVhBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(rankingProfile: UserPublicProfile, position: Int, isLastItem: Boolean){
        binding.rankingProfile = rankingProfile
        binding.position = position
        if (isLastItem) binding.divider.gone()
        binding.executePendingBindings()
    }

    companion object{
        fun from(parent: ViewGroup): RankingsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RankingProfileVhBinding.inflate(layoutInflater, parent, false)
            return RankingsViewHolder(binding)
        }
    }
}