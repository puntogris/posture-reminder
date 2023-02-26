package com.puntogris.posture.domain.repository

import androidx.activity.result.ActivityResult
import com.puntogris.posture.domain.model.LoginResult
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun authWithGoogle(result: ActivityResult): Flow<LoginResult>

    suspend fun signOutUser(): SimpleResult

    suspend fun getShowLoginPref(): Boolean

    suspend fun getShowWelcomeFlowPref(): Boolean
}
