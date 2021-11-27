package com.puntogris.posture.feature_main.data.repository

import android.os.Build
import com.puntogris.posture.common.alarm.Alarm
import com.puntogris.posture.common.alarm.Notifications
import com.puntogris.posture.feature_main.data.datasource.local.DataStore
import com.puntogris.posture.feature_main.data.datasource.local.db.ReminderDao
import com.puntogris.posture.feature_main.data.datasource.remote.FirebaseClients
import com.puntogris.posture.feature_main.domain.model.Reminder
import com.puntogris.posture.feature_main.domain.model.ReminderId
import com.puntogris.posture.feature_main.domain.repository.ReminderRepository
import com.puntogris.posture.feature_main.domain.repository.ReminderServerApi
import com.puntogris.posture.common.utils.DispatcherProvider
import com.puntogris.posture.common.utils.IDGenerator
import com.puntogris.posture.common.utils.Result
import com.puntogris.posture.common.utils.SimpleResult
import com.puntogris.posture.common.workers.WorkersManager
import kotlinx.coroutines.withContext

class ReminderRepositoryImpl(
    private val firebase: FirebaseClients,
    private val reminderServerApi: ReminderServerApi,
    private val reminderDao: ReminderDao,
    private val notifications: Notifications,
    private val alarm: Alarm,
    private val dataStore: DataStore,
    private val dispatchers: DispatcherProvider,
    private val workersManager: WorkersManager,
) : ReminderRepository {

    override suspend fun deleteReminder(reminder: Reminder): SimpleResult =
        withContext(dispatchers.io) {
            SimpleResult.build {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notifications.removeNotificationChannelWithId(reminder.reminderId)
                }
                if (reminder.uid.isNotBlank()) {
                    reminderServerApi.deleteReminder(reminder.reminderId)
                }
                if (reminderDao.getActiveReminder() == reminder) {
                    alarm.cancelAlarms()
                }
                reminderDao.delete(reminder)
            }
        }

    override suspend fun insertReminder(reminder: Reminder): Result<ReminderId> =
        withContext(dispatchers.io) {
            Result.build {

                if (reminder.reminderId.isBlank()) {
                    reminder.reminderId = IDGenerator.randomID()
                    firebase.currentUid?.let {
                        reminder.uid = it
                    }
                }

                reminderDao.insert(reminder)

                reminderDao.getActiveReminder()?.let {
                    if (it == reminder && dataStore.isAlarmActive()) {
                        alarm.refreshAlarms(reminder)
                    }
                }

                if (reminder.uid.isNotBlank()) {
                    workersManager.launchUploadReminderWorker(reminder.reminderId)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notifications.createChannelForReminderSdkO(reminder)
                }

                ReminderId(reminder.reminderId)
            }
        }

    override fun getRemindersLiveData() = reminderDao.getAllRemindersLiveData()

    override fun getActiveReminderLiveData() = reminderDao.getActiveReminderLiveData()

    override suspend fun getActiveReminder() = withContext(dispatchers.io) {
        reminderDao.getActiveReminder()
    }

    override suspend fun syncReminder(reminderId: String) {
        reminderDao.getReminderWithId(reminderId)?.let {
            reminderServerApi.insertReminder(it)
        }
    }
}