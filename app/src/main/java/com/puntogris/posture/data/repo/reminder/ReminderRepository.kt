package com.puntogris.posture.data.repo.reminder

import android.content.Context
import android.os.Build
import androidx.work.*
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.alarm.Notifications
import com.puntogris.posture.workers.UploadReminderWorker
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.remote.FirebaseReminderDataSource
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.ReminderId
import com.puntogris.posture.model.Result
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.utils.Constants.REMINDER_ID_WORKER_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

class ReminderRepository @Inject constructor(
    private val firebase: FirebaseReminderDataSource,
    private val reminderDao: ReminderDao,
    private val notifications: Notifications,
    private val alarm: Alarm,
    @ApplicationContext private val context: Context
): IReminderRepository {

    override fun getAllRemindersFromRoomLiveData() = reminderDao.getAllRemindersLiveData()

    override suspend fun deleteReminder(reminder: Reminder): SimpleResult = withContext(Dispatchers.IO){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifications.removeNotificationChannelWithId(reminder.reminderId)
            }
            if (reminder.uid.isNotBlank()) {
                firebase.getReminderDocumentRefWithId(reminder.reminderId).delete().await()
            }
            reminderDao.delete(reminder)

            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }

    override suspend fun insertReminder(reminder: Reminder): Result<ReminderId> = withContext(Dispatchers.IO){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifications.createChannelForReminderSdkO(reminder)
            }
            if (reminder.reminderId.isBlank()) fillIdsIfNewReminder(reminder)
            reminderDao.insert(reminder)

            if (reminder.uid.isNotBlank()){
                registerFirestoreUploadReminderWorker(reminder.reminderId)
            }

            reminderDao.getActiveReminder()?.let {
                if (it.reminderId == reminder.reminderId) alarm.refreshAlarms(reminder)
            }
            Result.Success(ReminderId(reminder.reminderId))
        }catch (e:Exception){
            Result.Error(e)
        }
    }

    private fun fillIdsIfNewReminder(reminder: Reminder){
        reminder.reminderId = firebase.getNewReminderDocumentRef().id
        if (firebase.currentUser() != null) firebase.getCurrentUserId()
    }

    private fun registerFirestoreUploadReminderWorker(reminderId: String){
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

    override suspend fun insertReminderIntoFirestoreFromRoom(reminderId: String){
        reminderDao.getReminderWithId(reminderId)?.let {
            firebase.getReminderDocumentRefWithId(reminderId).set(it).await()
        }
    }
}