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
            checkForLatestDataAndInsertUser(user)
            UserAccount.Registered
        }
    }

    private suspend fun checkForLatestDataAndInsertUser(user: UserPrivateData){
        val roomUser = userDao.getUser()
        if (roomUser.id == roomUser.id && roomUser.experience > user.experience) return
        else userDao.insert(user)
    }

    private suspend fun insertNewUserIntoRoomAndFirestore(user: UserPrivateData) {
        userDao.insert(user)
        val userPublicProfileRef = firestoreUser.getUserPublicProfileRef()
        val userPrivateDataRef = firestoreUser.getUserPrivateDataRef()
        firestoreUser.runBatch().apply {
            set(userPrivateDataRef, user)
            set(userPublicProfileRef, getPublicProfileFromUserPrivateData(user))
        }.commit().await()
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

    override suspend fun syncUserExperienceInFirestoreWithRoom() {
        val roomUser = userDao.getUser()
        firestoreUser.runBatch().apply {
            update(firestoreUser.getUserPublicProfileRef(),"experience", roomUser.experience)
            update(firestoreUser.getUserPrivateDataRef(),"experience", roomUser.experience)
        }.commit().await()
    }

}