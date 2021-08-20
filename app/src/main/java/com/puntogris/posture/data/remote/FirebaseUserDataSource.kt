package com.puntogris.posture.data.remote

import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(): FirebaseDataSource() {

    fun getUserPrivateDataRef() =
        firestore
        .collection("users")
        .document(getCurrentUserId())

    fun getUserPublicProfileRef() =
        firestore
            .collection("users")
            .document(getCurrentUserId())
            .collection("public_profile")
            .document("profile")



}