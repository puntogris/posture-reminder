package com.puntogris.posture.domain.repository

import com.puntogris.posture.domain.model.UserPublicProfile
import com.puntogris.posture.utils.Result
import kotlinx.coroutines.flow.Flow

interface RankingsRepository {
    fun getRankingsWithLimit(limit: Long): Flow<Result<List<UserPublicProfile>>>
}