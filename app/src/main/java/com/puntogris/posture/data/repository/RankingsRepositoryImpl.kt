package com.puntogris.posture.data.repository

import com.puntogris.posture.data.DispatcherProvider
import com.puntogris.posture.data.datasource.remote.FirebaseRankingDataSource
import com.puntogris.posture.domain.repository.RankingsRepository
import com.puntogris.posture.model.UserPublicProfile
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.puntogris.posture.utils.Result

class RankingsRepositoryImpl(
    private val rankingsFirebase: FirebaseRankingDataSource,
    private val dispatchers: DispatcherProvider
) : RankingsRepository {

    override suspend fun getAllRankingsServer(): Result<List<UserPublicProfile>> =
        withContext(dispatchers.io) {
            Result.build {
                rankingsFirebase.getRankingsQueryWithLimit()
                    .get()
                    .await()
                    .toObjects(UserPublicProfile::class.java)
            }
        }

    override suspend fun getTopThreeRankingsServer(): Result<List<UserPublicProfile>> =
        withContext(dispatchers.io) {
            Result.build {
                rankingsFirebase.getRankingsQueryWithLimit(3)
                    .get()
                    .await()
                    .toObjects(UserPublicProfile::class.java)
            }
        }

}