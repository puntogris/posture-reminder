package com.puntogris.posture.ui.notification

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repo.user.UserRepository
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.utils.Constants.EXPERIENCE_PER_NOTIFICATION
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    suspend fun updateDayLogWithReward() {
        val log = DayLog(expGained = EXPERIENCE_PER_NOTIFICATION, notifications = 1)
        userRepository.updateRoomDayLogAndUser(log)
    }
}