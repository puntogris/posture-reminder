package com.puntogris.posture.data.repo.rankings

import com.puntogris.posture.model.RepoResult
import com.puntogris.posture.model.UserPublicProfile

interface IRankingsRepository {
    suspend fun getAllRankingsFirestore(): RepoResult<List<UserPublicProfile>>
    suspend fun getTopThreeRankingsFirestore(): RepoResult<List<UserPublicProfile>>
}