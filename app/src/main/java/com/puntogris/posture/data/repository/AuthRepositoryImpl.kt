package com.puntogris.posture.data.repository

import androidx.activity.result.ActivityResult
import androidx.work.WorkManager
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.db.UserDao
import com.puntogris.posture.data.datasource.remote.AuthServerApi
import com.puntogris.posture.data.datasource.remote.GoogleSingInApi
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.repository.AuthRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.SYNC_ACCOUNT_WORKER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val workManager: WorkManager,
    private val authServerApi: AuthServerApi,
    private val dataStore: DataStore,
    private val userDao: UserDao,
    private val googleSingInApi: GoogleSingInApi,
    private val alarm: Alarm,
    private val dispatchers: DispatcherProvider
) : AuthRepository {

    override suspend fun getShowLoginPref() = dataStore.showLoginPref()

    override fun serverAuthWithGoogle(result: ActivityResult): Flow<LoginResult> = flow {
        try {
            emit(LoginResult.InProgress)
            val credential = googleSingInApi.getCredentialWithIntent(requireNotNull(result.data))
            val authResult = authServerApi.signInWithCredential(credential)
            emit(LoginResult.Success(UserPrivateData.from(authResult.user)))
        } catch (e: Exception) {
            googleSingInApi.signOut()
            emit(LoginResult.Error)
        }
    }

    override suspend fun signOutUser() = SimpleResult.build {
        alarm.cancelAlarms()
        authServerApi.signOut()
        googleSingInApi.signOut()
        dataStore.setShowLoginPref(true)
        workManager.cancelUniqueWork(SYNC_ACCOUNT_WORKER)
    }

    override suspend fun singInAnonymously() = withContext(dispatchers.io) {
        SimpleResult.build {
            userDao.insert(UserPrivateData())
            dataStore.setShowLoginPref(false)
        }
    }
}