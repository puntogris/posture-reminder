package com.puntogris.posture.feature_auth.data.data_source

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.puntogris.posture.common.data.data_source.remote.FirebaseClients
import com.puntogris.posture.feature_auth.domain.repository.AuthServerApi
import kotlinx.coroutines.tasks.await

class FirebaseAuthApi(private val firebaseClients: FirebaseClients) : AuthServerApi {

    override suspend fun signInWithCredential(credential: AuthCredential): AuthResult {
        return firebaseClients.auth.signInWithCredential(credential).await()
    }

    override fun signOut() {
        firebaseClients.auth.signOut()
    }
}