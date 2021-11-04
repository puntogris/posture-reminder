package com.puntogris.posture.domain.repository

import androidx.lifecycle.LiveData
import com.puntogris.posture.utils.Result
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.ReminderId
import com.puntogris.posture.utils.SimpleResult

interface ReminderRepository {
    fun getAllLocalRemindersLiveData(): LiveData<List<Reminder>>
    suspend fun deleteReminder(reminder: Reminder): SimpleResult
    suspend fun insertReminder(reminder: Reminder): Result<Exception, ReminderId>
    fun getActiveReminderLiveData(): LiveData<Reminder?>
    suspend fun getActiveReminder(): Reminder?
    suspend fun insertLocalReminderToServer(reminderId: String)
}