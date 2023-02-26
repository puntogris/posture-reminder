package com.puntogris.posture.domain.model

sealed class LoginResult {
    object InProgress : LoginResult()
    class Success(val userPrivateData: UserPrivateData) : LoginResult()
    object Error : LoginResult()
}
