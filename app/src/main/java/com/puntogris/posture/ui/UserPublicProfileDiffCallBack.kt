package com.puntogris.posture.ui

import androidx.recyclerview.widget.DiffUtil
import com.puntogris.posture.model.UserPublicProfile

class UserPublicProfileDiffCallBack: DiffUtil.ItemCallback<UserPublicProfile>() {

    override fun areItemsTheSame(oldItem: UserPublicProfile, newItem: UserPublicProfile): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserPublicProfile, newItem: UserPublicProfile): Boolean {
        return  oldItem == newItem
    }
}