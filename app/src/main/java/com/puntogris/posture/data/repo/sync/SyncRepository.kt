package com.puntogris.posture.data.repo.sync

import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.local.UserDao
import com.puntogris.posture.data.remote.FirebaseReminderDataSource
import com.puntogris.posture.data.remote.FirebaseUserDataSource
import com.puntogris.posture.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val reminderDao: ReminderDao,
    private val userDao: UserDao,
    private val firestoreUser: FirebaseUserDataSource,
    private val firestoreReminder: FirebaseReminderDataSource
) : ISyncRepository {

    override suspend fun syncFirestoreAccountWithRoom(userPrivateData: UserPrivateData): SimpleResult =
        withContext(Dispatchers.IO) {
            try {
                val userState = checkIfUserIsNewAndCreateIfNot(userPrivateData)
                if (userState is UserAccount.Registered) {
                    syncUserReminders()
                }
                SimpleResult.Success
            } catch (e: Exception) {
                SimpleResult.Failure
            }
        }


    private suspend fun checkIfUserIsNewAndCreateIfNot(user: UserPrivateData): UserAccount {
        val userDocument = firestoreUser.getUserPrivateDataRef().get().await()
        return if (!userDocument.exists()) {
            insertNewUserIntoRoomAndFirestore(user)
            UserAccount.New
        } else {
            insertUserIntoRoom(user)
            UserAccount.Registered
        }
    }

    private suspend fun insertNewUserIntoRoomAndFirestore(user: UserPrivateData) {
        val userPublicProfileRef = firestoreUser.getUserPublicProfileRef()
        val userPrivateDataRef = firestoreUser.getUserPrivateDataRef()
        insertUserIntoRoom(user)
        firestoreUser.runBatch().apply {
            set(userPrivateDataRef, user)
            set(userPublicProfileRef, getPublicProfileFromUserPrivateData(user))
        }.commit().await()
    }

    private suspend fun insertUserIntoRoom(user: UserPrivateData){
        userDao.insert(user)
    }

    private fun getPublicProfileFromUserPrivateData(user: UserPrivateData): UserPublicProfile {
        return UserPublicProfile(
            username = user.username,
            country = user.country,
            id = user.id
        )
    }

    private suspend fun syncUserReminders() {
        val reminders =
            firestoreReminder.getUserRemindersQuery().get().await().toObjects(Reminder::class.java)
        reminderDao.insertRemindersIfNotInRoom(reminders)
    }

    override suspend fun syncFirestoreWithRoomUserPublicProfile() {
        val roomUser = userDao.getUser()
        firestoreUser.getUserPublicProfileRef().update("experience", roomUser.experience).await()
    }

}