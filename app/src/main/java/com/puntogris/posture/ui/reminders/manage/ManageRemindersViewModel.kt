package com.puntogris.posture.ui.reminders.manage

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.local.UserDao
import com.puntogris.posture.data.repo.reminder.ReminderRepository
import com.puntogris.posture.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageRemindersViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
): ViewModel() {

    fun getAllReminders() = reminderRepository.getAllRemindersFromRoomLiveData()

    suspend fun deleteReminder(reminder: Reminder) = reminderRepository.deleteReminder(reminder)

    suspend fun insertReminder(reminder: Reminder) = reminderRepository.insertReminder(reminder)

    suspend fun updateCurrentReminder(reminderId: String) = reminderRepository.updateCurrentReminder(reminderId)
}