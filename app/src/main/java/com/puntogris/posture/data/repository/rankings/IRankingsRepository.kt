package com.puntogris.posture.data.repository.rankings

import com.puntogris.posture.model.UserPublicProfile
import com.puntogris.posture.utils.Result

interface IRankingsRepository {
    suspend fun getAllRankingsFirestore(): Result<Exception, List<UserPublicProfile>>
    suspend fun getTopThreeRankingsFirestore(): Result<Exception, List<UserPublicProfile>>
}