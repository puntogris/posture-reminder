package com.puntogris.posture.data.repo.reminder

import android.content.Context
import android.os.Build
import androidx.work.*
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.alarm.Notifications
import com.puntogris.posture.workers.UploadReminderWorker
import com.puntogris.posture.data.datasource.local.room.dao.ReminderDao
import com.puntogris.posture.data.datasource.remote.FirebaseReminderDataSource
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.ReminderId
import com.puntogris.posture.utils.Constants.REMINDER_ID_WORKER_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.SimpleResult

class ReminderRepository @Inject constructor(
    private val firebase: FirebaseReminderDataSource,
    private val reminderDao: ReminderDao,
    private val notifications: Notifications,
    private val alarm: Alarm,
    @ApplicationContext private val context: Context
): IReminderRepository {

    override fun getAllRemindersFromRoomLiveData() = reminderDao.getAllRemindersLiveData()

    override suspend fun deleteReminder(reminder: Reminder): SimpleResult = withContext(Dispatchers.IO){
        SimpleResult.build {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifications.removeNotificationChannelWithId(reminder.reminderId)
            }
            if (reminder.uid.isNotBlank()) {
                firebase.getReminderDocumentRefWithId(reminder.reminderId).delete().await()
            }
            if (reminderDao.getActiveReminder() == reminder){
                alarm.cancelAlarms()
            }
            reminderDao.delete(reminder)
        }
    }

    override suspend fun insertReminder(reminder: Reminder): Result<Exception, ReminderId> = withContext(Dispatchers.IO){
        Result.build {

            if (reminder.reminderId.isBlank()) {
                fillIdsIfNewReminder(reminder)
            }
            if (reminder.uid.isNotBlank()){
                registerUploadServerReminderWorker(reminder.reminderId)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifications.createChannelForReminderSdkO(reminder)
            }

            reminderDao.insert(reminder)
            reminderDao.getActiveReminder()?.let {
                if (it.reminderId == reminder.reminderId) alarm.refreshAlarms(reminder)
            }

            ReminderId(reminder.reminderId)
        }
    }

    private fun fillIdsIfNewReminder(reminder: Reminder){
        reminder.reminderId = firebase.getNewReminderDocumentRef().id
        firebase.currentUser()?.let {
            reminder.uid = it.uid
        }
    }

    private fun registerUploadServerReminderWorker(reminderId: String){
        val reminderData = Data.Builder().putString(REMINDER_ID_WORKER_DATA, reminderId).build()
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val uploadReminder = OneTimeWorkRequestBuilder<UploadReminderWorker>()
            .setInputData(reminderData)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(uploadReminder)
    }

    override fun getActiveReminderLiveData() = reminderDao.getActiveReminderLiveData()

    override suspend fun getActiveReminder() = withContext(Dispatchers.IO) {
        reminderDao.getActiveReminder()
    }

    override suspend fun insertLocalReminderToServer(reminderId: String){
        reminderDao.getReminderWithId(reminderId)?.let {
            firebase.getReminderDocumentRefWithId(reminderId).set(it).await()
        }
    }
}