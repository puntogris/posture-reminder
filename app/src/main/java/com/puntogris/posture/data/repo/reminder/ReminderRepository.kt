package com.puntogris.posture.data.repo.reminder

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.ReminderWorker
import com.puntogris.posture.UploadWorker
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.remote.FirebaseReminderDataSource
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.SimpleResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderFirestore: FirebaseReminderDataSource,
    private val reminderDao: ReminderDao,
    @ApplicationContext private val context: Context
): IReminderRepository {

    override fun getAllRemindersFromRoomLiveData() = reminderDao.getAllRemindersLiveData()

    override suspend fun deleteReminder(reminder: Reminder) :SimpleResult = withContext(Dispatchers.IO){
        try {
            reminderDao.delete(reminder)
            reminderFirestore.getReminderDocumentRefWithId(reminder.id).delete().await()
            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }

    override suspend fun insertReminder(reminder: Reminder): SimpleResult = withContext(Dispatchers.IO){
        try {
            if (reminder.id.isBlank()) fillIdsIfNewReminder(reminder)
            reminderDao.insert(reminder)
            registerFirestoreUploadReminder(reminder.id)
            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }

    private fun fillIdsIfNewReminder(reminder: Reminder){
        val ref = reminderFirestore.getNewReminderDocumentRef()
        reminder.apply {
            id = ref.id
            uid = reminderFirestore.getCurrentUserId()
        }
    }

    private fun registerFirestoreUploadReminder(reminderId: String){
        val uploadReminder = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(Data.Builder().putString("reminderId", reminderId).build())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
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