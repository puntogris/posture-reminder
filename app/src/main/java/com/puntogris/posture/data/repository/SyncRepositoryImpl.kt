package com.puntogris.posture.data.repository

import com.lyft.kronos.KronosClock
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.db.AppDatabase
import com.puntogris.posture.data.datasource.remote.FirebaseClients
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.repository.ReminderServerApi
import com.puntogris.posture.domain.repository.SyncRepository
import com.puntogris.posture.domain.repository.UserServerApi
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.workers.WorkersManager
import kotlinx.coroutines.withContext

class SyncRepositoryImpl(
    private val firebaseClients: FirebaseClients,
    private val appDatabase: AppDatabase,
    private val userServerApi: UserServerApi,
    private val reminderServerApi: ReminderServerApi,
    private val dataStore: DataStore,
    private val dispatchers: DispatcherProvider,
    private val workersManager: WorkersManager,
    private val kronosClock: KronosClock,
) : SyncRepository {

    override suspend fun syncServerAccountWithLocalDb(loginUser: UserPrivateData): SimpleResult =
        withContext(dispatchers.io) {
            SimpleResult.build {
                val serverUser = getUserFromServer()
                if (serverUser != null) {
                    compareLatestUserData(serverUser)
                    syncUserReminders()
                } else {
                    insertNewUser(loginUser)
                }
                workersManager.launchSyncAccountWorker()
                dataStore.setShowLoginPref(false)
            }
        }

    private suspend fun getUserFromServer(): UserPrivateData? {
        return userServerApi.getUser()
    }

    private suspend fun compareLatestUserData(serverUser: UserPrivateData) {
        val localUser = appDatabase.userDao.getUser()
        if (
            localUser == null ||
            localUser.uid != serverUser.uid ||
            localUser.experience < serverUser.experience
        ) {
            appDatabase.userDao.insert(serverUser)
        }
    }

    private suspend fun insertNewUser(user: UserPrivateData) {
        appDatabase.userDao.insert(user)
        userServerApi.createUser(user)
    }

    private suspend fun syncUserReminders() {
        insertServerRemindersIntoLocal()
        insertLocalRemindersIntoServer()
    }

    private suspend fun insertServerRemindersIntoLocal() {
        val reminders = reminderServerApi.getReminders()
        appDatabase.reminderDao.insertRemindersIfNotInRoom(reminders)
    }

    private suspend fun insertLocalRemindersIntoServer() {
        val localReminders = appDatabase.reminderDao.getRemindersNotSynced()

        if (localReminders.isNotEmpty()) {
            val uid = requireNotNull(firebaseClients.currentUid)

            localReminders.forEach { it.uid = uid }
            reminderServerApi.saveReminders(localReminders)
            appDatabase.reminderDao.insert(localReminders)
        }
    }

    override suspend fun syncUserExperienceInServerWithLocalDb() {
        appDatabase.userDao.getUser()?.let {
            val serverTime = kronosClock.getCurrentNtpTimeMs() ?: return
            val expAmount = it.calculateMaxExpPermitted(serverTime)
            userServerApi.updateExperience(expAmount)
        }
    }
}