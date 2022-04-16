package com.puntogris.posture.ui.account

import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    userRepository: UserRepository,
    dayLogsRepository: DayLogsRepository
) : ViewModel() {

    val user = userRepository.getUserLiveData()

    val weekData = dayLogsRepository.getWeekDayLogs()
}