package com.puntogris.posture.data.repo.login

import android.content.Intent
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.StateFlow

interface ILoginRepository {
    fun firebaseAuthWithGoogle(idToken: String): StateFlow<LoginResult>
    fun createGoogleSignInIntent(): Intent
    suspend fun signOutUserFromFirebaseAndGoogle(): SimpleResult
    suspend fun singInAnonymously(): SimpleResult
}