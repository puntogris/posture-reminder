package com.puntogris.posture.domain.repository

import androidx.activity.result.ActivityResult
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun serverAuthWithGoogle(result: ActivityResult): Flow<LoginResult>

    suspend fun signOutUser(): SimpleResult

    suspend fun getShowLoginPref(): Boolean

    suspend fun getShowWelcomePref(): Boolean
}