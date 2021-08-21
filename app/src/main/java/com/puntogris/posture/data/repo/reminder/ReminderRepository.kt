package com.puntogris.posture.data.repo.reminder

import com.google.firebase.firestore.DocumentReference
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.remote.FirebaseReminderDataSource
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderFirestore: FirebaseReminderDataSource,
    private val reminderDao: ReminderDao
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
            val reminderRef = getReminderRef(reminder)
            reminderDao.insert(reminder)
            reminderRef.set(reminder).await()
            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }

    private fun getReminderRef(reminder: Reminder): DocumentReference{
        return if (reminder.id.isBlank()) getDocumentRefForNewReminder(reminder)
        else reminderFirestore.getReminderDocumentRefWithId(reminder.id)
    }

    private fun getDocumentRefForNewReminder(reminder: Reminder): DocumentReference{
        val ref = reminderFirestore.getNewReminderDocumentRef()
        reminder.apply {
            id = ref.id
            uid = reminderFirestore.getCurrentUserId()
        }
        return ref
    }

    override fun getActiveReminderLiveData() = reminderDao.getActiveReminderLiveData()


}