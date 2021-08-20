package com.puntogris.posture.ui.account

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repo.day_logs.DayLogsRepository
import com.puntogris.posture.data.repo.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    userRepository: UserRepository,
    private val dayLogsRepository: DayLogsRepository
): ViewModel() {

    val user = userRepository.getUserLiveDataRoom()

    suspend fun getWeekData() = dayLogsRepository.getWeekDayLogs()


}