package com.puntogris.posture.data.repository

import androidx.work.*
import com.lyft.kronos.KronosClock
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.db.ReminderDao
import com.puntogris.posture.data.datasource.local.db.UserDao
import com.puntogris.posture.data.datasource.remote.FirebaseClients
import com.puntogris.posture.data.datasource.remote.ReminderServerApi
import com.puntogris.posture.data.datasource.remote.UserServerApi
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.repository.SyncRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.SYNC_ACCOUNT_WORKER
import com.puntogris.posture.workers.SyncAccountWorker
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncRepositoryImpl(
    private val firebaseClients: FirebaseClients,
    private val reminderDao: ReminderDao,
    private val userDao: UserDao,
    private val userServerApi: UserServerApi,
    private val reminderServerApi: ReminderServerApi,
    private val dataStore: DataStore,
    private val dispatchers: DispatcherProvider,
    private val workManager: WorkManager,
    private val kronosClock: KronosClock,
) : SyncRepository {

    override suspend fun syncSeverAccountWithLocalDb(loginUser: UserPrivateData): SimpleResult =
        withContext(dispatchers.io) {
            SimpleResult.build {
                val serverUser = getUserFromServer()
                if (serverUser != null) {
                    compareLatestUserData(serverUser)
                    syncUserReminders()
                } else {
                    insertNewUser(loginUser)
                }
                setupSyncAccountWorkManager()
                dataStore.setShowLoginPref(false)
            }
        }

    private suspend fun getUserFromServer(): UserPrivateData? {
        return userServerApi.getUser()
    }

    private suspend fun compareLatestUserData(serverUser: UserPrivateData) {
        val localUser = userDao.getUser()
        if (
            localUser == null ||
            localUser.uid != serverUser.uid ||
            localUser.experience < serverUser.experience
        ) {
            userDao.insert(serverUser)
        }
    }

    private suspend fun insertNewUser(user: UserPrivateData) {
        userDao.insert(user)
        userServerApi.createUser(user)
    }

    private suspend fun syncUserReminders() {
        insertServerRemindersIntoLocal()
        insertLocalRemindersIntoServer()
    }

    private suspend fun insertServerRemindersIntoLocal() {
        val reminders = reminderServerApi.getReminders()
        reminderDao.insertRemindersIfNotInRoom(reminders)
    }

    private suspend fun insertLocalRemindersIntoServer() {
        val localReminders = reminderDao.getRemindersNotSynced()

        if (localReminders.isNotEmpty()) {
            val uid = requireNotNull(firebaseClients.currentUid)

            localReminders.forEach { it.uid = uid }
            reminderServerApi.saveReminders(localReminders)
            reminderDao.insert(localReminders)
        }
    }

    override suspend fun syncUserExperienceInServerWithLocalDb() {
        userDao.getUser()?.let {
            val serverTime = kronosClock.getCurrentNtpTimeMs() ?: return
            val expAmount = it.calculateMaxExpPermitted(serverTime)
            userServerApi.updateExperience(expAmount)
        }
    }

    private suspend fun setupSyncAccountWorkManager() {
        val syncWork = PeriodicWorkRequestBuilder<SyncAccountWorker>(5, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            SYNC_ACCOUNT_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            syncWork
        ).await()
    }
}