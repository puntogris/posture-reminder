package com.puntogris.posture.data.repository

import androidx.activity.result.ActivityResult
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.remote.GoogleSingInApi
import com.puntogris.posture.data.datasource.toUserPrivateData
import com.puntogris.posture.domain.model.LoginResult
import com.puntogris.posture.domain.repository.AuthRepository
import com.puntogris.posture.domain.repository.AuthServerApi
import com.puntogris.posture.framework.alarm.Alarm
import com.puntogris.posture.framework.workers.WorkersManager
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.SYNC_ACCOUNT_WORKER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl(
    private val workersManager: WorkersManager,
    private val authServerApi: AuthServerApi,
    private val dataStore: DataStore,
    private val googleSingInApi: GoogleSingInApi,
    private val alarm: Alarm,
    private val dispatchers: DispatcherProvider
) : AuthRepository {

    override suspend fun getShowLoginPref() = dataStore.showLoginPref()

    override suspend fun getShowWelcomePref() = dataStore.showWelcomePref()

    override fun authWithGoogle(result: ActivityResult): Flow<LoginResult> = flow {
        try {
            emit(LoginResult.InProgress)
            val credential = googleSingInApi.getCredentialWithIntent(requireNotNull(result.data))
            val authResult = authServerApi.signInWithCredential(credential)
            val user = requireNotNull(authResult.user).toUserPrivateData()
            emit(LoginResult.Success(user))
        } catch (e: Exception) {
            googleSingInApi.signOut()
            emit(LoginResult.Error)
        }
    }.flowOn(dispatchers.io)

    override suspend fun signOutUser() = SimpleResult.build {
        alarm.cancelAlarms()
        authServerApi.signOut()
        googleSingInApi.signOut()
        dataStore.setShowLoginPref(true)
        workersManager.cancelWorker(SYNC_ACCOUNT_WORKER)
    }

}