package com.puntogris.posture.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.data.repo.day_logs.DayLogsRepository
import com.puntogris.posture.data.repo.reminder.ReminderRepository
import com.puntogris.posture.data.repo.user.UserRepository
import com.puntogris.posture.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    userRepository: UserRepository,
    private val dayLogsRepository: DayLogsRepository,
    private val reminderRepository: ReminderRepository
): ViewModel() {


    val user = userRepository.getUserLiveDataRoom()

    suspend fun getWeekData() = dayLogsRepository.getWeekDayLogs()

    fun getActiveReminder() = reminderRepository.getActiveReminderLiveData()


}