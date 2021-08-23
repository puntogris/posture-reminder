package com.puntogris.posture.data.repo.reminder

import android.content.Context
import android.os.Build
import androidx.work.*
import com.puntogris.posture.Notifications
import com.puntogris.posture.UploadReminderWorker
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.remote.FirebaseReminderDataSource
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.utils.Constants.REMINDER_ID_WORKER_DATA
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderFirestore: FirebaseReminderDataSource,
    private val reminderDao: ReminderDao,
    private val notifications: Notifications,
    @ApplicationContext private val context: Context
): IReminderRepository {

    override fun getAllRemindersFromRoomLiveData() = reminderDao.getAllRemindersLiveData()

    override suspend fun deleteReminder(reminder: Reminder) :SimpleResult = withContext(Dispatchers.IO){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifications.removeNotificationChannelWithId(reminder.id)
            }
            reminderDao.delete(reminder)
            reminderFirestore.getReminderDocumentRefWithId(reminder.id).delete().await()
            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }

    override suspend fun insertReminder(reminder: Reminder): SimpleResult = withContext(Dispatchers.IO){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifications.createChannelForReminderSdkO(reminder)
            }
            if (reminder.id.isBlank()) fillIdsIfNewReminder(reminder)
            reminderDao.insert(reminder)
            registerFirestoreUploadReminder(reminder.id)
            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }

    private fun fillIdsIfNewReminder(reminder: Reminder){
        reminder.apply {
            id = reminderFirestore.getNewReminderDocumentRef().id
            uid = reminderFirestore.getCurrentUserId()
        }
    }

    private fun registerFirestoreUploadReminder(reminderId: String){
        val reminderData = Data.Builder().putString(REMINDER_ID_WORKER_DATA, reminderId).build()
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val uploadReminder = OneTimeWorkRequestBuilder<UploadReminderWorker>()
            .setInputData(reminderData)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(uploadReminder)
    }

    override fun getActiveReminderLiveData() = reminderDao.getActiveReminderLiveData()

    override suspend fun getActiveReminder() = reminderDao.getActiveReminder()

    override suspend fun insertReminderIntoFirestoreFromRoom(reminderId: String){
        reminderDao.getReminderWithId(reminderId)?.let {
            reminderFirestore.getReminderDocumentRefWithId(reminderId).set(it).await()
        }
    }

}