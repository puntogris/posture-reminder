package com.puntogris.posture.data.repository

import android.os.Build
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.data.datasource.local.db.ReminderDao
import com.puntogris.posture.data.datasource.remote.FirebaseClients
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.ReminderId
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.domain.repository.ReminderServerApi
import com.puntogris.posture.framework.alarm.Alarm
import com.puntogris.posture.framework.alarm.Notifications
import com.puntogris.posture.framework.workers.WorkersManager
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.withContext

class ReminderRepositoryImpl(
    private val firebase: FirebaseClients,
    private val reminderServerApi: ReminderServerApi,
    private val reminderDao: ReminderDao,
    private val notifications: Notifications,
    private val alarm: Alarm,
    private val dataStoreHelper: DataStoreHelper,
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

                firebase.currentUid?.let {
                    reminder.uid = it
                }
                reminderDao.insert(reminder)

                reminderDao.getActiveReminder()
                    ?.takeIf { it == reminder && dataStoreHelper.isAlarmActive() }
                    ?.let { alarm.refreshAlarms(it) }

                if (reminder.uid.isNotBlank()) {
                    workersManager.launchUploadReminderWorker(reminder.reminderId)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notifications.createNotificationChannel(reminder)
                }
                ReminderId(reminder.reminderId)
            }
        }

    override fun getRemindersStream() = reminderDao.getAllRemindersStream()

    override fun getActiveReminderStream() = reminderDao.getActiveReminderStream()

    override suspend fun getActiveReminder() = withContext(dispatchers.io) {
        reminderDao.getActiveReminder()
    }

    override suspend fun syncReminder(reminderId: String) {
        reminderDao.getReminderWithId(reminderId)?.let {
            reminderServerApi.insertReminder(it)
        }
    }
}