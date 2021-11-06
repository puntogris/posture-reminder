package com.puntogris.posture.utils

import com.puntogris.posture.domain.model.UserPrivateData

sealed class LoginResult {
    object InProgress : LoginResult()
    class Success(val userPrivateData: UserPrivateData) : LoginResult()
    object Error : LoginResult()
}

