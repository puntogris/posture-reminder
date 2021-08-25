package com.puntogris.posture.data.repo.sync

import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.local.UserDao
import com.puntogris.posture.data.remote.FirebaseReminderDataSource
import com.puntogris.posture.data.remote.FirebaseUserDataSource
import com.puntogris.posture.model.*
import com.puntogris.posture.utils.Constants.EXPERIENCE_FIELD
import com.puntogris.posture.utils.Constants.MAX_EXPERIENCE_OFFSET
import com.puntogris.posture.utils.Constants.MAX_EXPERIENCE_PER_DAY
import com.puntogris.posture.utils.toDays
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
            uid = user.uid
        )
    }

    private suspend fun syncUserReminders() {
        val reminders =
            firestoreReminder.getUserRemindersQuery().get().await().toObjects(Reminder::class.java)
        reminderDao.insertRemindersIfNotInRoom(reminders)
    }

    //reformat functions, naming and order
    override suspend fun syncUserExperienceInFirestoreWithRoom() {
        val roomUser = userDao.getUser()

        roomUser?.let {
            val expAmount = getMaxExpPermitted(it)

              if (expAmount != null){
                  firestoreUser.runBatch().apply {
                      update(firestoreUser.getUserPublicProfileRef(), EXPERIENCE_FIELD, expAmount)
                      update(firestoreUser.getUserPrivateDataRef(), EXPERIENCE_FIELD, expAmount)
                  }.commit().await()
              }
        }
    }

    private suspend fun getMaxExpPermitted(user: UserPrivateData): Int?{
        val creationTimestampMillis = user.creationDate.toDate().time
        val serverTimestampMillis = getDateFromServer()

        return if (serverTimestampMillis != null){
            val daysDiff = (serverTimestampMillis - creationTimestampMillis).toDays()

            //2 days worth of exp as offset just in case
            val maxExpPermitted = daysDiff * MAX_EXPERIENCE_PER_DAY + MAX_EXPERIENCE_OFFSET

            if (user.experience > maxExpPermitted) maxExpPermitted else user.experience
        }else null

    }

    private suspend fun getDateFromServer(): Long? {
        return firestoreUser.functions
            .getHttpsCallable("getServerTimestamp")
            .call()
            .await()
            .data as? Long
    }
}