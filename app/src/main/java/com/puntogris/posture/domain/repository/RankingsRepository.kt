package com.puntogris.posture.domain.repository

import com.puntogris.posture.model.UserPublicProfile
import com.puntogris.posture.utils.Result

interface RankingsRepository {
    suspend fun getAllRankingsServer(): Result<Exception, List<UserPublicProfile>>
    suspend fun getTopThreeRankingsServer(): Result<Exception, List<UserPublicProfile>>
}