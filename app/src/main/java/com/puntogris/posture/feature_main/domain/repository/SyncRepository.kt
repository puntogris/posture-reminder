package com.puntogris.posture.feature_main.domain.repository

import com.puntogris.posture.feature_auth.domain.model.UserPrivateData
import com.puntogris.posture.common.utils.SimpleResult

interface SyncRepository {

    suspend fun syncAccount(authUser: UserPrivateData?): SimpleResult

    suspend fun syncAccountExperience()
}
