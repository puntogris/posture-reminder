package com.puntogris.posture.feature_auth.domain.model

import com.puntogris.posture.feature_main.domain.model.UserPrivateData

sealed class LoginResult {
    object InProgress : LoginResult()
    class Success(val userPrivateData: UserPrivateData) : LoginResult()
    object Error : LoginResult()
}

