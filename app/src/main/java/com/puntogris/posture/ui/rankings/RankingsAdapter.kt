package com.puntogris.posture.ui.rankings

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.puntogris.posture.domain.model.UserPublicProfile

class RankingsAdapter : ListAdapter<UserPublicProfile, RankingsViewHolder>(
    UserPublicProfileDiffCallBack()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingsViewHolder {
        return RankingsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RankingsViewHolder, position: Int) {
        val isLastItem = currentList.size - 1 == position
        holder.bind(getItem(position), position, isLastItem)
    }
}