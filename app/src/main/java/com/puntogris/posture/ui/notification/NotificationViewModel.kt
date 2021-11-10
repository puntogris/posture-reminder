package com.puntogris.posture.ui.notification

import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.utils.RewardExp
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_NOTIFICATION
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val logsRepository: DayLogsRepository
) : ViewModel() {

    suspend fun updateDayLogWithReward(): RewardExp {
        val log = DayLog(expGained = EXPERIENCE_PER_NOTIFICATION, notifications = 1)
        return logsRepository.updateDayLogAndUser(log)
    }
}