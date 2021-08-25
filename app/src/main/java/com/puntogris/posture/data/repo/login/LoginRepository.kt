package com.puntogris.posture.data.repo.login

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.data.remote.FirebaseLoginDataSource
import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.model.LoginResult
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.utils.DataStore
import com.puntogris.posture.utils.capitalizeWords
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

class LoginRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginFirestore: FirebaseLoginDataSource,
    private val dataStore: DataStore
): ILoginRepository {

    private fun getGoogleSignInClient(): GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    override fun firebaseAuthWithGoogle(idToken: String): StateFlow<LoginResult> {
        val result = MutableStateFlow<LoginResult>(LoginResult.InProgress)
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        loginFirestore.auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val firestoreUser = getUserPrivateDataFromFirebaseUser(it.user)
                result.value = LoginResult.Success(firestoreUser)
            }
            .addOnFailureListener { result.value = LoginResult.Error(it.localizedMessage) }

        return result
    }

    private fun getUserPrivateDataFromFirebaseUser(user:FirebaseUser?): UserPrivateData{
        val date = user?.metadata?.creationTimestamp
        val timestamp = if (date == null) Timestamp.now() else Timestamp((Date(date)))
        return UserPrivateData(
            username = user?.displayName.toString().capitalizeWords(),
            uid = user?.uid.toString(),
            email = user?.email.toString(),
            photoUrl = user?.photoUrl.toString(),
            creationDate = timestamp
        )
    }

    override fun createGoogleSignInIntent(): Intent{
        return getGoogleSignInClient().signInIntent
    }

    override suspend fun signOutUserFromFirebaseAndGoogle(): SimpleResult {
        return try {
            dataStore.setLoginCompletedPref(false)
            loginFirestore.logOutFromFirebase()
            getGoogleSignInClient().signOut()
            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }
}