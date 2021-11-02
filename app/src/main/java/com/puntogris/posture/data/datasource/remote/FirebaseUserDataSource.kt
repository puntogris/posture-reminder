package com.puntogris.posture.data.datasource.remote

import com.puntogris.posture.utils.Constants.PUBLIC_PROFILE_COL_GROUP
import com.puntogris.posture.utils.Constants.PUBLIC_PROFILE_DOC
import com.puntogris.posture.utils.Constants.USERS_COLLECTION
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor() : FirebaseDataSource() {

    fun getUserPrivateDataRef() =
        firestore
            .collection(USERS_COLLECTION)
            .document(getCurrentUserId())

    fun getUserPublicProfileRef() =
        firestore
            .collection(USERS_COLLECTION)
            .document(getCurrentUserId())
            .collection(PUBLIC_PROFILE_COL_GROUP)
            .document(PUBLIC_PROFILE_DOC)

}