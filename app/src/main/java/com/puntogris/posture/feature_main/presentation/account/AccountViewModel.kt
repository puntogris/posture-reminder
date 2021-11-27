package com.puntogris.posture.feature_main.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.puntogris.posture.feature_main.domain.repository.DayLogsRepository
import com.puntogris.posture.feature_main.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    userRepository: UserRepository,
    private val dayLogsRepository: DayLogsRepository
) : ViewModel() {

    val user = userRepository.getUserLiveData()

    val weekData = liveData {
        emit(dayLogsRepository.getWeekDayLogs())
    }
}