package com.puntogris.posture.ui.notification

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repo.day_logs.DayLogsRepository
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.utils.Constants.EXPERIENCE_PER_NOTIFICATION
import com.puntogris.posture.utils.RewardExp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val logsRepository: DayLogsRepository
): ViewModel() {

    suspend fun updateDayLogWithReward(): RewardExp {
        val log = DayLog(expGained = EXPERIENCE_PER_NOTIFICATION, notifications = 1)
        return logsRepository.updateRoomDayLogAndUser(log)
    }
}