package com.puntogris.posture.feature_main.presentation.sync

import androidx.lifecycle.ViewModel
import com.puntogris.posture.feature_main.domain.model.UserPrivateData
import com.puntogris.posture.feature_auth.domain.repository.AuthRepository
import com.puntogris.posture.feature_main.domain.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SyncAccountViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun synAccountWith(userPrivateData: UserPrivateData?) =
        syncRepository.syncAccount(userPrivateData)

    suspend fun logOut() {
        authRepository.signOutUser()
    }

    suspend fun showWelcome() = authRepository.getShowWelcomePref()
}