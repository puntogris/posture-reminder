package com.puntogris.posture.ui.notification

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repo.user.UserRepository
import com.puntogris.posture.model.DayLog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    suspend fun updateUserExp(experience: Int) {
        val log = DayLog(expGained = experience, notifications = 1)
        userRepository.updateUserExperienceRoom(log)
    }
}