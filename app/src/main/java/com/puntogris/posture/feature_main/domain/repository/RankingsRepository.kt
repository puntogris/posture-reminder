package com.puntogris.posture.feature_main.domain.repository

import com.puntogris.posture.feature_main.domain.model.UserPublicProfile
import com.puntogris.posture.common.utils.Result
import kotlinx.coroutines.flow.Flow

interface RankingsRepository {
    fun getRankingsWithLimit(limit: Long): Flow<Result<List<UserPublicProfile>>>
}