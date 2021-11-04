package com.puntogris.posture.domain.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LoginRepository {
    fun serverAuthWithGoogle(result: ActivityResult): Flow<LoginResult>
    fun getGoogleSignInIntent(): Intent
    suspend fun signOutUser(): SimpleResult
    suspend fun singInAnonymously(): SimpleResult
    suspend fun getShowLoginPref(): Boolean
}