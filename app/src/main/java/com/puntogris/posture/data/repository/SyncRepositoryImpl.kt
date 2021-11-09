package com.puntogris.posture.data.repository

import androidx.work.*
import com.lyft.kronos.KronosClock
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.db.ReminderDao
import com.puntogris.posture.data.datasource.local.db.UserDao
import com.puntogris.posture.data.datasource.remote.FirebaseReminderDataSource
import com.puntogris.posture.data.datasource.remote.FirebaseUserDataSource
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.model.UserPublicProfile
import com.puntogris.posture.domain.repository.SyncRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_FIELD
import com.puntogris.posture.utils.constants.Constants.SYNC_ACCOUNT_WORKER
import com.puntogris.posture.workers.SyncAccountWorker
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncRepositoryImpl(
    private val reminderDao: ReminderDao,
    private val userDao: UserDao,
    private val firestoreUser: FirebaseUserDataSource,
    private val firestoreReminder: FirebaseReminderDataSource,
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
        return firestoreUser
            .getUserPrivateDataRef()
            .get()
            .await()
            .toObject(UserPrivateData::class.java)
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

        firestoreUser.runBatch().apply {
            set(firestoreUser.getUserPrivateDataRef(), user)
            set(firestoreUser.getUserPublicProfileRef(), UserPublicProfile.from(user))
        }.commit().await()
    }

    private suspend fun syncUserReminders() {
        insertServerRemindersIntoLocal()
        insertLocalRemindersIntoServer()
    }

    private suspend fun insertServerRemindersIntoLocal() {
        val firestoreReminders = firestoreReminder
            .getUserRemindersQuery()
            .get()
            .await()
            .toObjects(Reminder::class.java)

        reminderDao.insertRemindersIfNotInRoom(firestoreReminders)
    }

    private suspend fun insertLocalRemindersIntoServer() {
        val localReminders = reminderDao.getRemindersNotSynced()

        if (localReminders.isNotEmpty()) {
            val uid = firestoreUser.getCurrentUserId()
            val batch = firestoreReminder.runBatch()

            localReminders.forEach { reminder ->
                reminder.uid = uid
                batch.set(
                    firestoreReminder.getReminderDocumentRefWithId(reminder.reminderId),
                    reminder
                )
            }

            reminderDao.insert(localReminders)
            batch.commit().await()
        }
    }

    override suspend fun syncUserExperienceInServerWithLocalDb() {
        userDao.getUser()?.let {
            val serverTime = kronosClock.getCurrentNtpTimeMs() ?: return
            val expAmount = it.calculateMaxExpPermitted(serverTime)

            firestoreUser.runBatch().apply {
                update(firestoreUser.getUserPublicProfileRef(), EXPERIENCE_FIELD, expAmount)
                update(firestoreUser.getUserPrivateDataRef(), EXPERIENCE_FIELD, expAmount)
            }.commit().await()
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