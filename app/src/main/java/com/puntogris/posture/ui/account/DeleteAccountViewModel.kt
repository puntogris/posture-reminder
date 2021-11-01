package com.puntogris.posture.ui.account

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repository.user.UserRepository
import javax.inject.Inject

class DeleteAccountViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    suspend fun deleteAccount() = userRepository.deleteUserAccountData()

    suspend fun getCurrentUser() = userRepository.getLocalUser()
}