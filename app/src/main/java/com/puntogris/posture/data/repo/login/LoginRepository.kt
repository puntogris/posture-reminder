package com.puntogris.posture.data.repo.login

import android.content.Context
import androidx.work.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.remote.FirebaseLoginDataSource
import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.utils.Constants.SYNC_ACCOUNT_WORKER
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.room.dao.UserDao
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.capitalizeWords
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class LoginRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginFirestore: FirebaseLoginDataSource,
    private val dataStore: DataStore,
    private val userDao: UserDao
): ILoginRepository {

    private fun getGoogleSignInClient(): GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    override fun firebaseAuthWithGoogle(idToken: String): StateFlow<LoginResult> =
        MutableStateFlow<LoginResult>(LoginResult.InProgress).also { flow ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            loginFirestore.auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    flow.value = LoginResult.Success(UserPrivateData.from(it.user))
                }
                .addOnFailureListener {
                    flow.value = LoginResult.Error
                }
        }


    override fun createGoogleSignInIntent() = getGoogleSignInClient().signInIntent

    override suspend fun signOutUserFromFirebaseAndGoogle() = SimpleResult.build {
        dataStore.setShowLoginPref(true)
        loginFirestore.logOutFromFirebase()
        getGoogleSignInClient().signOut()
        WorkManager.getInstance(context).cancelUniqueWork(SYNC_ACCOUNT_WORKER)
    }


    override suspend fun singInAnonymously() = withContext(Dispatchers.IO){
        SimpleResult.build {
            val user = UserPrivateData(username = context.getString(R.string.human))
            userDao.insert(user)
            dataStore.setShowLoginPref(false)
        }
    }

    override suspend fun getShowLoginPref() = dataStore.showLoginPref()
}