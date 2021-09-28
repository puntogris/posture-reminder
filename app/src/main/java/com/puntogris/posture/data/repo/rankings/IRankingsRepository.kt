package com.puntogris.posture.data.repo.rankings

import com.puntogris.posture.model.UserPublicProfile
import com.puntogris.posture.utils.Result

interface IRankingsRepository {
    suspend fun getAllRankingsFirestore(): Result<List<UserPublicProfile>>
    suspend fun getTopThreeRankingsFirestore(): Result<List<UserPublicProfile>>
}