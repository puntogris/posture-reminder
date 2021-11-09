package com.puntogris.posture.data.repository

import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.data.datasource.local.db.AppDatabase
import com.puntogris.posture.data.datasource.remote.FirebaseClients
import com.puntogris.posture.data.datasource.remote.FirebaseUserApi
import com.puntogris.posture.data.datasource.remote.UserServerApi
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.repository.UserRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.USER_NAME_FIELD
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val firebaseClients: FirebaseClients,
    private val userServerApi: UserServerApi,
    private val alarm: Alarm,
    private val dispatchers: DispatcherProvider,
    private val appDatabase: AppDatabase
) : UserRepository {

    override fun isUserLoggedIn() = firebaseClients.getCurrentUser() != null

    override fun getLocalUserLiveData() = appDatabase.userDao.getUserLiveData()

    override suspend fun getLocalUser() = appDatabase.userDao.getUser()

    override suspend fun updateLocalAndServerUsername(name: String) = withContext(dispatchers.io) {
        SimpleResult.build {
            userServerApi.updateUsername(name)
            appDatabase.userDao.updateUsername(name)
        }
    }

    override suspend fun updateLocalActiveReminder(reminder: Reminder) {
        appDatabase.userDao.updateCurrentUserReminder(reminder.reminderId)
        alarm.refreshAlarms(reminder)
    }

    override suspend fun deleteUserAccountData(): SimpleResult = withContext(dispatchers.io) {
        SimpleResult.build {
            userServerApi.deleteAccount()
            appDatabase.clearAllTables()
        }
    }
}