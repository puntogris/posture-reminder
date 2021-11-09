package com.puntogris.posture.data.datasource.remote

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult

interface AuthServerApi {
    suspend fun signInWithCredential(credential: AuthCredential): AuthResult
    fun signOut()
}