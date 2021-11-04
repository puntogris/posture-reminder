package com.puntogris.posture.data.datasource.remote

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.puntogris.posture.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleSingInDataSource @Inject constructor(
    @ApplicationContext private val context: Context
){

    private fun getGoogleSignInClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    suspend fun getCredentialWithIntent(intent: Intent): AuthCredential {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent).await().idToken
        return GoogleAuthProvider.getCredential(task, null)
    }

    suspend fun signOut(){
        getGoogleSignInClient().signOut().await()
    }

    fun createSignIntent() = getGoogleSignInClient().signInIntent

}