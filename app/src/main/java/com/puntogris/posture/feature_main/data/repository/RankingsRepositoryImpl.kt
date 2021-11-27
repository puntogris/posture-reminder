package com.puntogris.posture.feature_main.data.repository

import com.google.firebase.firestore.Query
import com.puntogris.posture.feature_main.data.datasource.remote.FirebaseClients
import com.puntogris.posture.feature_main.domain.model.UserPublicProfile
import com.puntogris.posture.feature_main.domain.repository.RankingsRepository
import com.puntogris.posture.common.utils.DispatcherProvider
import com.puntogris.posture.common.utils.Result
import com.puntogris.posture.common.utils.constants.Constants
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class RankingsRepositoryImpl(
    private val firebase: FirebaseClients,
    private val dispatchers: DispatcherProvider
) : RankingsRepository {

    override fun getRankingsWithLimit(limit: Long) = flow<Result<List<UserPublicProfile>>> {
        try {
            val result = firebase.firestore
                .collectionGroup(Constants.PUBLIC_PROFILE_COL_GROUP)
                .orderBy(Constants.EXPERIENCE_FIELD, Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
                .toObjects(UserPublicProfile::class.java)

            emit(Result.Success(result))

        } catch (e: Exception) {
            emit(Result.Error())
        }
    }.flowOn(dispatchers.io)
}