package com.puntogris.posture.feature_main.presentation.rankings

import androidx.recyclerview.widget.DiffUtil
import com.puntogris.posture.feature_main.domain.model.UserPublicProfile

class UserPublicProfileDiffCallBack : DiffUtil.ItemCallback<UserPublicProfile>() {

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