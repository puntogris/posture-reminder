package com.puntogris.posture.ui.rankings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.databinding.RankingProfileVhBinding
import com.puntogris.posture.domain.model.UserPublicProfile
import com.puntogris.posture.utils.extensions.gone
import com.puntogris.posture.utils.setProfileRankingMedal
import com.puntogris.posture.utils.setRankingLevel

class RankingsViewHolder(private val binding: RankingProfileVhBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(rankingProfile: UserPublicProfile, position: Int, isLastItem: Boolean) {
        with(binding) {
            rankingMedalImage.setProfileRankingMedal(position)
            rankingUsername.text = rankingProfile.username
            rankingUserPosition.text = position.toString()
            binding.rankingUserExp.setRankingLevel(rankingProfile.experience)
            if (isLastItem) divider.gone()
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