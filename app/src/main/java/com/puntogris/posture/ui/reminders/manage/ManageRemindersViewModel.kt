package com.puntogris.posture.ui.reminders.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.SelectableReminder
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageRemindersViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val activeReminder = reminderRepository.getActiveReminderLiveData()

    private val savedReminders = reminderRepository.getRemindersLiveData()

    val reminders = combine(activeReminder.asFlow(), savedReminders.asFlow()) { active, reminders ->
        reminders.map {
            SelectableReminder(
                reminder = it,
                isSelected = it.reminderId == active?.reminderId
            )
        }
    }

    suspend fun deleteReminder(reminder: Reminder) = reminderRepository.deleteReminder(reminder)

    fun insertReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.insertReminder(reminder)
        }
    }

    suspend fun updateReminder(reminder: Reminder) = userRepository.updateActiveReminder(reminder)
}
