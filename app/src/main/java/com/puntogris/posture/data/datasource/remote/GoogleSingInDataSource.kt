package com.puntogris.posture.data.datasource.remote

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleSingInDataSource @Inject constructor(private val googleSignInClient: GoogleSignInClient) {

    suspend fun signOut() {
        googleSignInClient.signOut().await()
    }

    suspend fun getCredentialWithIntent(intent: Intent): AuthCredential {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent).await().idToken
        return GoogleAuthProvider.getCredential(task, null)
    }
}