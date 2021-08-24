package com.puntogris.posture.ui

import androidx.recyclerview.widget.DiffUtil
import com.puntogris.posture.model.UserPublicProfile

class UserPublicProfileDiffCallBack: DiffUtil.ItemCallback<UserPublicProfile>() {

    override fun areItemsTheSame(oldItem: UserPublicProfile, newItem: UserPublicProfile): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: UserPublicProfile, newItem: UserPublicProfile): Boolean {
        return  oldItem == newItem
    }
}