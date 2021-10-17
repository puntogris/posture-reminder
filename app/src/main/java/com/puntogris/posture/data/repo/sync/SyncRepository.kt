package com.puntogris.posture.data.repo.sync

import android.content.Context
import androidx.work.*
import com.puntogris.posture.data.datasource.local.room.dao.ReminderDao
import com.puntogris.posture.data.datasource.local.room.dao.UserDao
import com.puntogris.posture.data.datasource.remote.FirebaseReminderDataSource
import com.puntogris.posture.data.datasource.remote.FirebaseUserDataSource
import com.puntogris.posture.model.*
import com.puntogris.posture.utils.Constants.EXPERIENCE_FIELD
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.utils.Constants.SERVER_TIMESTAMP_FUNCTION
import com.puntogris.posture.utils.Constants.SYNC_ACCOUNT_WORKER
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.workers.SyncAccountWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val reminderDao: ReminderDao,
    private val userDao: UserDao,
    private val firestoreUser: FirebaseUserDataSource,
    private val firestoreReminder: FirebaseReminderDataSource,
    private val dataStore: DataStore,
    @ApplicationContext private val context: Context
) : ISyncRepository {

    override suspend fun syncFirestoreAccountWithRoom(userPrivateData: UserPrivateData): SimpleResult =
        withContext(Dispatchers.IO) {
            SimpleResult.build {
                val serverUser = getUserFromServer()
                if (serverUser != null) {
                    compareLatestUserData(serverUser)
                    syncUserReminders()
                } else {
                    insertNewUser(userPrivateData)
                }
                dataStore.setShowLoginPref(false)
                setupSyncAccountWorkManager()
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
        insertOnlineRemindersIntoLocalDb()
        checkForNotSyncedLocalReminders()
    }

    private suspend fun insertOnlineRemindersIntoLocalDb() {
        val firestoreReminders = firestoreReminder
            .getUserRemindersQuery()
            .get()
            .await()
            .toObjects(Reminder::class.java)

        reminderDao.insertRemindersIfNotInRoom(firestoreReminders)
    }

    private suspend fun checkForNotSyncedLocalReminders() {
        val roomReminders = reminderDao.getAllEmptyReminders()

        if (roomReminders.isNotEmpty()) {
            val uid = firestoreUser.getCurrentUserId()
            val batch = firestoreReminder.runBatch()

            roomReminders.forEach {
                it.uid = uid
                batch.set(firestoreReminder.getReminderDocumentRefWithId(it.reminderId), it)
            }

            reminderDao.insert(roomReminders)
            batch.commit().await()
        }
    }

    override suspend fun syncUserExperienceInFirestoreWithRoom() {
        userDao.getUser()?.let {
            val expAmount = it.getMaxExpPermittedWithServerTimestamp(getTimestampFromServer())

            if (expAmount != null) {
                firestoreUser.runBatch().apply {
                    update(firestoreUser.getUserPublicProfileRef(), EXPERIENCE_FIELD, expAmount)
                    update(firestoreUser.getUserPrivateDataRef(), EXPERIENCE_FIELD, expAmount)
                }.commit().await()
            }
        }
    }

    private suspend fun getTimestampFromServer(): Long? {
        return firestoreUser.functions
            .getHttpsCallable(SERVER_TIMESTAMP_FUNCTION)
            .call()
            .await()
            .data as? Long
    }

    private fun setupSyncAccountWorkManager() {
        val syncWork = PeriodicWorkRequestBuilder<SyncAccountWorker>(5, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                SYNC_ACCOUNT_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                syncWork
            )
    }
}