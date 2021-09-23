package com.puntogris.posture.data.repo.sync

import android.content.Context
import androidx.work.*
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.local.UserDao
import com.puntogris.posture.data.remote.FirebaseReminderDataSource
import com.puntogris.posture.data.remote.FirebaseUserDataSource
import com.puntogris.posture.model.*
import com.puntogris.posture.utils.Constants
import com.puntogris.posture.utils.Constants.EXPERIENCE_FIELD
import com.puntogris.posture.utils.DataStore
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
            try {
                val userState = checkIfUserIsNewAndCreateIfNot(userPrivateData)
                if (userState is UserAccount.Registered) {
                    syncUserReminders()
                }
                setupSyncAccountWorkManager()
                dataStore.setLoginCompletedPref(true)

                SimpleResult.Success
            } catch (e: Exception) {
                dataStore.setLoginCompletedPref(false)
                SimpleResult.Failure
            }
        }

    private suspend fun checkIfUserIsNewAndCreateIfNot(user: UserPrivateData): UserAccount {
        val userDocument = firestoreUser.getUserPrivateDataRef().get().await()
        return if (!userDocument.exists()) {
            insertNewUserIntoRoomAndFirestore(user)
            UserAccount.New
        } else {
            checkForLatestDataAndInsertUser(userDocument.toObject(UserPrivateData::class.java)!!)
            UserAccount.Registered
        }
    }

    private suspend fun checkForLatestDataAndInsertUser(user: UserPrivateData){
        val roomUser = userDao.getUser()
        if (roomUser != null && roomUser.uid == roomUser.uid && roomUser.experience > user.experience) return
        else userDao.insert(user)
    }

    private suspend fun insertNewUserIntoRoomAndFirestore(user: UserPrivateData) {
        userDao.insert(user)

        firestoreUser.runBatch().apply {
            set(firestoreUser.getUserPrivateDataRef(), user)
            set(firestoreUser.getUserPublicProfileRef(), UserPublicProfile.from(user))
        }.commit().await()
    }

    private suspend fun syncUserReminders() {
        syncOnlineRemindersWithLocalDb()
        syncAndUpdateLocalReminders()
    }

    private suspend fun syncOnlineRemindersWithLocalDb(){
        val firestoreReminders = firestoreReminder
            .getUserRemindersQuery()
            .get()
            .await()
            .toObjects(Reminder::class.java)

        reminderDao.insertRemindersIfNotInRoom(firestoreReminders)
    }
    
    private suspend fun syncAndUpdateLocalReminders(){
        val roomReminders = reminderDao.getAllEmptyReminders()

        if (roomReminders.isNotEmpty()){
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

            if (expAmount != null){
              firestoreUser.runBatch().apply {
                  update(firestoreUser.getUserPublicProfileRef(), EXPERIENCE_FIELD, expAmount)
                  update(firestoreUser.getUserPrivateDataRef(), EXPERIENCE_FIELD, expAmount)
              }.commit().await()
            }
        }
    }

    private suspend fun getTimestampFromServer(): Long? {
        return firestoreUser.functions
            .getHttpsCallable("getServerTimestamp")
            .call()
            .await()
            .data as? Long
    }

    private fun setupSyncAccountWorkManager(){
        val syncWork = PeriodicWorkRequestBuilder<SyncAccountWorker>(5, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(Constants.SYNC_ACCOUNT_WORKER, ExistingPeriodicWorkPolicy.KEEP, syncWork)
    }
}