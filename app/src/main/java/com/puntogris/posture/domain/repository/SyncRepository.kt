package com.puntogris.posture.domain.repository

import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.utils.SimpleResult

interface SyncRepository {
    suspend fun syncSeverAccountWithLocalDb(loginUser: UserPrivateData): SimpleResult
    suspend fun syncUserExperienceInServerWithLocalDb()
}