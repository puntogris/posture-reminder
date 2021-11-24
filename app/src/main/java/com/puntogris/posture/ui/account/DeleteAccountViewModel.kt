package com.puntogris.posture.ui.account

import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.repository.AuthRepository
import com.puntogris.posture.domain.repository.UserRepository
import com.puntogris.posture.utils.SimpleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun deleteAccount(): SimpleResult {
        return userRepository.deleteUserAccount().let {
            if (it is SimpleResult.Success) return authRepository.signOutUser() else it
        }
    }

    suspend fun getCurrentUser() = userRepository.getUser()
}