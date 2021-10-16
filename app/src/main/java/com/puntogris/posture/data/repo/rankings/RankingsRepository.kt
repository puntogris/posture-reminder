package com.puntogris.posture.data.repo.rankings

import com.puntogris.posture.data.datasource.remote.FirebaseRankingDataSource
import com.puntogris.posture.model.UserPublicProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.puntogris.posture.utils.Result

class RankingsRepository @Inject constructor(
    private val rankingsFirestore: FirebaseRankingDataSource
): IRankingsRepository {

    override suspend fun getAllRankingsFirestore(): Result<Exception, List<UserPublicProfile>> = withContext(Dispatchers.IO) {
        Result.build {
            rankingsFirestore.getRankingsQueryWithLimit()
                .get()
                .await()
                .toObjects(UserPublicProfile::class.java)
        }
    }

    override suspend fun getTopThreeRankingsFirestore(): Result<Exception, List<UserPublicProfile>> = withContext(Dispatchers.IO) {
        Result.build {
            rankingsFirestore.getRankingsQueryWithLimit(3)
                .get()
                .await()
                .toObjects(UserPublicProfile::class.java)
        }
    }

}