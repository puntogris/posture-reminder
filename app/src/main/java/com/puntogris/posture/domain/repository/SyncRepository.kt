package com.puntogris.posture.domain.repository

import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.utils.SimpleResult

interface SyncRepository {

    suspend fun syncServerAccountWithLocalDb(loginUser: UserPrivateData): SimpleResult

    suspend fun syncUserExperienceInServerWithLocalDb()
}
