package com.puntogris.posture.ui.account

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repo.day_logs.DayLogsRepository
import com.puntogris.posture.data.repo.reminder.ReminderRepository
import com.puntogris.posture.data.repo.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    userRepository: UserRepository,
    private val dayLogsRepository: DayLogsRepository,
    private val reminderRepository: ReminderRepository
): ViewModel() {

    val user = userRepository.getLocalUserLiveData()

    suspend fun getWeekData() = dayLogsRepository.getWeekDayLogs()

    fun getActiveReminder() = reminderRepository.getActiveReminderLiveData()
}