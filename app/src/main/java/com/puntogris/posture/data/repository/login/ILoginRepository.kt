package com.puntogris.posture.data.repository.login

import android.content.Intent
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.StateFlow

interface ILoginRepository {
    fun firebaseAuthWithGoogle(idToken: String): StateFlow<LoginResult>
    fun getGoogleSignInIntent(): Intent
    suspend fun signOutUser(): SimpleResult
    suspend fun singInAnonymously(): SimpleResult
    suspend fun getShowLoginPref(): Boolean
}