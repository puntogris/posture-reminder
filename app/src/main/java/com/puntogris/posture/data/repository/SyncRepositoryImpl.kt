package com.puntogris.posture.data.repository

import com.lyft.kronos.KronosClock
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.data.datasource.local.db.AppDatabase
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.repository.ReminderServerApi
import com.puntogris.posture.domain.repository.SyncRepository
import com.puntogris.posture.domain.repository.UserServerApi
import com.puntogris.posture.framework.workers.WorkersManager
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.withContext

class SyncRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val userServerApi: UserServerApi,
    private val reminderServerApi: ReminderServerApi,
    private val dataStoreHelper: DataStoreHelper,
    private val dispatchers: DispatcherProvider,
    private val workersManager: WorkersManager,
    private val kronosClock: KronosClock,
) : SyncRepository {

    override suspend fun syncAccount(authUser: UserPrivateData?): SimpleResult =
        withContext(dispatchers.io) {
            SimpleResult.build {
                if (authUser != null) {
                    val serverUser = userServerApi.getUser()
                    if (serverUser != null) {
                        syncLatestUserData(serverUser)
                        syncUserReminders()
                    } else {
                        val localUser = appDatabase.userDao.getUser()
                        if (localUser == null) {
                            appDatabase.userDao.insert(authUser)
                        } else {
                            appDatabase.userDao.updateCurrentUserData(
                                authUser.uid,
                                authUser.username,
                                authUser.email,
                                authUser.photoUrl
                            )
                        }
                        userServerApi.insertUser(authUser)
                        insertLocalRemindersIntoServer()
                    }
                    workersManager.launchSyncAccountWorker()
                } else {
                    appDatabase.userDao.insert(UserPrivateData())
                }
                dataStoreHelper.setShowLoginPref(false)
            }
        }

    private suspend fun syncLatestUserData(serverUser: UserPrivateData) {
        val localUser = appDatabase.userDao.getUser()
        if (
            localUser == null ||
            localUser.uid != serverUser.uid ||
            localUser.experience < serverUser.experience
        ) {
            appDatabase.userDao.insert(serverUser)
        }
    }

    private suspend fun syncUserReminders() {
        insertServerRemindersIntoLocal()
        insertLocalRemindersIntoServer()
    }

    private suspend fun insertServerRemindersIntoLocal() {
        val reminders = reminderServerApi.getReminders()
        appDatabase.reminderDao.insertIfNotInRoom(reminders)
    }

    private suspend fun insertLocalRemindersIntoServer() {
        appDatabase.reminderDao.getRemindersNotSynced().takeIf { it.isNotEmpty() }?.let {
            it.forEach { reminder -> reminder.uid = appDatabase.userDao.getUserId() }
            reminderServerApi.insertReminders(it)
            appDatabase.reminderDao.insert(it)
        }
    }

    override suspend fun syncAccountExperience() {
        appDatabase.userDao.getUser().takeIf { it != null && it.uid.isNotEmpty() }?.let {
            val serverTime = kronosClock.getCurrentNtpTimeMs() ?: return
            val expAmount = it.calculateMaxExpPermitted(serverTime)
            userServerApi.updateExperience(expAmount)
        }
    }
}