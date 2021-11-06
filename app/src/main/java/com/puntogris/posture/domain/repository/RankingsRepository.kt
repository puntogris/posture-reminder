package com.puntogris.posture.domain.repository

import com.puntogris.posture.domain.model.UserPublicProfile
import com.puntogris.posture.utils.Result

interface RankingsRepository {

    suspend fun getAllRankingsServer(): Result<List<UserPublicProfile>>

    suspend fun getTopThreeRankingsServer(): Result<List<UserPublicProfile>>
}