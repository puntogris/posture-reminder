package com.puntogris.posture.ui.rankings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.RankingProfileVhBinding
import com.puntogris.posture.domain.model.UserPublicProfile
import com.puntogris.posture.utils.extensions.getLevel
import com.puntogris.posture.utils.setProfileRankingMedal
import com.puntogris.posture.utils.setProfileRankingNumber

class RankingsViewHolder(private val binding: RankingProfileVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(rankingProfile: UserPublicProfile) {
        with(binding) {
            imageViewRankingMedalIcon.setProfileRankingMedal(adapterPosition)
            textViewRankingUsername.text = rankingProfile.username
            textViewRankingUserPosition.setProfileRankingNumber(adapterPosition)
            textViewRankingUserExp.text = rankingProfile.experience.getLevel().toString()
        }
    }

    companion object {
        fun from(parent: ViewGroup): RankingsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RankingProfileVhBinding.inflate(layoutInflater, parent, false)
            return RankingsViewHolder(binding)
        }
    }
}