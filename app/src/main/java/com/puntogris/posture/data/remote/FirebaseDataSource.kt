package com.puntogris.posture.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FirebaseDataSource @Inject constructor(){

    val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()

    fun getCurrentUserId() = auth.uid.toString()

    fun getCurrentUser() = auth.currentUser

    fun runBatch() = firestore.batch()

}