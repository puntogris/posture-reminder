package com.puntogris.posture.domain.repository

import androidx.lifecycle.LiveData
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.ReminderId
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getRemindersStream(): LiveData<List<Reminder>>

    fun getActiveReminderStream(): Flow<Reminder?>

    suspend fun getActiveReminder(): Reminder?

    suspend fun deleteReminder(reminder: Reminder): SimpleResult

    suspend fun insertReminder(reminder: Reminder): Result<ReminderId>

    suspend fun syncReminder(reminderId: String)
}