package com.puntogris.posture.domain.model

import androidx.annotation.Keep

@Keep
data class UserPublicProfile(
    var username: String = "",
    var country: String = "",
    var experience: Int = 0,
    var uid: String = ""
) {
    companion object {
        fun from(user: UserPrivateData): UserPublicProfile {
            return UserPublicProfile(
                username = user.username,
                country = user.country,
                uid = user.uid
            )
        }
    }
}