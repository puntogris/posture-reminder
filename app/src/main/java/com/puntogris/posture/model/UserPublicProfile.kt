package com.puntogris.posture.model

import androidx.annotation.Keep

@Keep
data class UserPublicProfile(
    var name: String = "",
    var country: String = "",
    var experience: Int = 0,
    var id: String = ""
)