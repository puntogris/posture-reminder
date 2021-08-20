package com.puntogris.posture.data.repo.reminder

import androidx.lifecycle.LiveData
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.SimpleResult

interface IReminderRepository {
    fun getAllRemindersFromRoomLiveData(): LiveData<List<Reminder>>
    suspend fun deleteReminder(reminder: Reminder): SimpleResult
    suspend fun insertReminder(reminder: Reminder): SimpleResult
    suspend fun updateCurrentReminder(reminderId: String)
}