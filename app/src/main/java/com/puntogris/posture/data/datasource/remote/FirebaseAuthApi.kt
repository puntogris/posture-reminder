package com.puntogris.posture.data.datasource.remote

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

class FirebaseAuthApi(private val firebaseClients: FirebaseClients) :AuthServerApi {

    override suspend fun signInWithCredential(credential: AuthCredential): AuthResult {
        return firebaseClients.auth.signInWithCredential(credential).await()
    }

    override fun signOut() {
        firebaseClients.auth.signOut()
    }
}