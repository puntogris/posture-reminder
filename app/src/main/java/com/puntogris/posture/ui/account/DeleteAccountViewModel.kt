package com.puntogris.posture.ui.account

import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.repository.LoginRepository
import com.puntogris.posture.domain.repository.UserRepository
import com.puntogris.posture.utils.SimpleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginRepository: LoginRepository
) : ViewModel() {

    suspend fun deleteAccount(): SimpleResult {
        val deleteAccount = userRepository.deleteUserAccountData()
        return if (deleteAccount is SimpleResult.Success) {
            loginRepository.signOutUser()
        } else {
            deleteAccount
        }
    }

    suspend fun getCurrentUser() = userRepository.getLocalUser()
}