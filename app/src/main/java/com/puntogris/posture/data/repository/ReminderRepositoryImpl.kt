package com.puntogris.posture.data.repository

import android.content.Context
import android.os.Build
import androidx.work.*
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.alarm.Notifications
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.db.ReminderDao
import com.puntogris.posture.data.datasource.remote.FirebaseReminderDataSource
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.ReminderId
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.REMINDER_ID_WORKER_DATA
import com.puntogris.posture.workers.UploadReminderWorker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReminderRepositoryImpl(
    private val firebase: FirebaseReminderDataSource,
    private val reminderDao: ReminderDao,
    private val notifications: Notifications,
    private val alarm: Alarm,
    private val dataStore: DataStore,
    private val dispatchers: DispatcherProvider,
    private val workManager: WorkManager,
) : ReminderRepository {

    override fun getAllLocalRemindersLiveData() = reminderDao.getAllRemindersLiveData()

    override suspend fun deleteReminder(reminder: Reminder): SimpleResult =
        withContext(dispatchers.io) {
            SimpleResult.build {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notifications.removeNotificationChannelWithId(reminder.reminderId)
                }
                if (reminder.uid.isNotBlank()) {
                    firebase.getReminderDocumentRefWithId(reminder.reminderId).delete().await()
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
                    fillIdsIfNewReminder(reminder)
                }
                if (reminder.uid.isNotBlank()) {
                    registerUploadServerReminderWorker(reminder.reminderId)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notifications.createChannelForReminderSdkO(reminder)
                }

                reminderDao.insert(reminder)
                reminderDao.getActiveReminder()?.let {
                    if (it.reminderId == reminder.reminderId && dataStore.isAlarmActive().first()) {
                        alarm.refreshAlarms(reminder)
                    }
                }

                ReminderId(reminder.reminderId)
            }
        }

    private fun fillIdsIfNewReminder(reminder: Reminder) {
        reminder.reminderId = firebase.getNewReminderDocumentRef().id
        firebase.currentUser()?.let {
            reminder.uid = it.uid
        }
    }

    private suspend fun registerUploadServerReminderWorker(reminderId: String) {
        val reminderData = Data.Builder().putString(REMINDER_ID_WORKER_DATA, reminderId).build()
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val uploadReminder = OneTimeWorkRequestBuilder<UploadReminderWorker>()
            .setInputData(reminderData)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(uploadReminder).await()
    }

    override fun getActiveReminderLiveData() = reminderDao.getActiveReminderLiveData()

    override suspend fun getActiveReminder() = withContext(dispatchers.io) {
        reminderDao.getActiveReminder()
    }

    override suspend fun insertLocalReminderToServer(reminderId: String) {
        reminderDao.getReminderWithId(reminderId)?.let {
            firebase.getReminderDocumentRefWithId(reminderId).set(it).await()
        }
    }
}