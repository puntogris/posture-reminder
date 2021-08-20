package com.puntogris.posture.data.remote

import com.google.firebase.firestore.Query
import javax.inject.Inject

class FirebaseRankingDataSource @Inject constructor(): FirebaseDataSource() {

    fun getRankingsQueryWithLimit(limit: Long = 30) =
        firestore.collectionGroup("public_profile")
            .orderBy("experience", Query.Direction.DESCENDING)
            .limit(limit)
}