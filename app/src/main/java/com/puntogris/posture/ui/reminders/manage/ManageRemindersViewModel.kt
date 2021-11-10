package com.puntogris.posture.ui.reminders.manage

import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageRemindersViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val savedReminders = reminderRepository.getAllLocalRemindersLiveData()

    suspend fun deleteReminder(reminder: Reminder) = reminderRepository.deleteReminder(reminder)

    suspend fun insertReminder(reminder: Reminder) = reminderRepository.insertReminder(reminder)

    suspend fun updateCurrentReminder(reminder: Reminder) =
        userRepository.updateActiveReminder(reminder)

}