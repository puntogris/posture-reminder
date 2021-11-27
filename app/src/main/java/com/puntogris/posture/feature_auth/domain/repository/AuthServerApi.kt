package com.puntogris.posture.feature_auth.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface AuthServerApi {

    suspend fun signInWithCredential(credential: AuthCredential): AuthResult

    fun signOut()
}