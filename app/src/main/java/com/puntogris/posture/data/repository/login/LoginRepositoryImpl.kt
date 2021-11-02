package com.puntogris.posture.data.repository.login

import android.content.Context
import androidx.work.WorkManager
import com.google.firebase.auth.GoogleAuthProvider
import com.puntogris.posture.R
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.room.dao.UserDao
import com.puntogris.posture.data.datasource.remote.FirebaseLoginDataSource
import com.puntogris.posture.data.datasource.remote.GoogleSingInDataSource
import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.utils.Constants.SYNC_ACCOUNT_WORKER
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class LoginRepositoryImpl(
    private val context: Context,
    private val loginFirebase: FirebaseLoginDataSource,
    private val dataStore: DataStore,
    private val userDao: UserDao,
    private val googleSingIn: GoogleSingInDataSource,
    private val alarm: Alarm
) : LoginRepository {

    override fun firebaseAuthWithGoogle(idToken: String): StateFlow<LoginResult> =
        MutableStateFlow<LoginResult>(LoginResult.InProgress).also { flow ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)

            loginFirebase.auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    flow.value = LoginResult.Success(UserPrivateData.from(it.user))
                }
                .addOnFailureListener {
                    flow.value = LoginResult.Error
                }
        }

    override fun getGoogleSignInIntent() = googleSingIn.createSignIntent()

    override suspend fun signOutUser() = SimpleResult.build {
        alarm.cancelAlarms()
        loginFirebase.signOut()
        googleSingIn.signOut()
        dataStore.setShowLoginPref(true)
        WorkManager.getInstance(context).cancelUniqueWork(SYNC_ACCOUNT_WORKER)
    }

    override suspend fun singInAnonymously() = withContext(Dispatchers.IO) {
        SimpleResult.build {
            val user = UserPrivateData(username = context.getString(R.string.human))
            userDao.insert(user)
            dataStore.setShowLoginPref(false)
        }
    }

    override suspend fun getShowLoginPref() = dataStore.showLoginPref()
}