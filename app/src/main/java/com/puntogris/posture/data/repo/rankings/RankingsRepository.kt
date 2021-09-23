package com.puntogris.posture.data.repo.rankings

import com.puntogris.posture.data.remote.FirebaseRankingDataSource
import com.puntogris.posture.utils.RepoResult
import com.puntogris.posture.model.UserPublicProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RankingsRepository @Inject constructor(
    private val rankingsFirestore: FirebaseRankingDataSource
): IRankingsRepository {

    override suspend fun getAllRankingsFirestore(): RepoResult<List<UserPublicProfile>> = withContext(Dispatchers.IO) {
        try {
            val result = rankingsFirestore.getRankingsQueryWithLimit()
                .get()
                .await()
                .toObjects(UserPublicProfile::class.java)
            RepoResult.Success(result)
        }catch (e:Exception){
            RepoResult.Error(e)
        }
    }

    override suspend fun getTopThreeRankingsFirestore(): RepoResult<List<UserPublicProfile>> = withContext(Dispatchers.IO) {
        try {
            val result = rankingsFirestore.getRankingsQueryWithLimit(3)
                .get()
                .await()
                .toObjects(UserPublicProfile::class.java)
            RepoResult.Success(result)
        }catch (e:Exception){
            RepoResult.Error(e)
        }
    }

}