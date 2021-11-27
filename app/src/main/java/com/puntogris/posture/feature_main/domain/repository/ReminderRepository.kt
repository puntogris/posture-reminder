package com.puntogris.posture.feature_main.domain.repository

import androidx.lifecycle.LiveData
import com.puntogris.posture.feature_main.domain.model.Reminder
import com.puntogris.posture.feature_main.domain.model.ReminderId
import com.puntogris.posture.common.utils.Result
import com.puntogris.posture.common.utils.SimpleResult

interface ReminderRepository {

    fun getRemindersLiveData(): LiveData<List<Reminder>>

    fun getActiveReminderLiveData(): LiveData<Reminder?>

    suspend fun getActiveReminder(): Reminder?

    suspend fun deleteReminder(reminder: Reminder): SimpleResult

    suspend fun insertReminder(reminder: Reminder): Result<ReminderId>

    suspend fun syncReminder(reminderId: String)
}