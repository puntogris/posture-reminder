package com.puntogris.posture.data.datasource.remote

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseClients @Inject constructor() {

    val firestore = Firebase.firestore
    val auth = Firebase.auth

    val currentUid: String?
        get() = auth.uid

    val getCurrentUser: FirebaseUser?
        get() = auth.currentUser
}