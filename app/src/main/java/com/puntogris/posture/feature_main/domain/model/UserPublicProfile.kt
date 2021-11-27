package com.puntogris.posture.feature_main.domain.model

import androidx.annotation.Keep

@Keep
data class UserPublicProfile(
    var username: String = "",

    var country: String = "",

    var experience: Int = 0,

    var uid: String = ""
)