package com.puntogris.posture.ui.rankings

import androidx.recyclerview.widget.DiffUtil
import com.puntogris.posture.domain.model.UserPublicProfile

class UserPublicProfileComparator : DiffUtil.ItemCallback<UserPublicProfile>() {

    override fun areItemsTheSame(oldItem: UserPublicProfile, newItem: UserPublicProfile): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(
        oldItem: UserPublicProfile,
        newItem: UserPublicProfile
    ): Boolean {
        return oldItem == newItem
    }
}