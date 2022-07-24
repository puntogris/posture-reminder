package com.puntogris.posture.ui.sync

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.repository.AuthRepository
import com.puntogris.posture.domain.repository.SyncRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class SyncAccountViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val authUser = savedStateHandle.get<UserPrivateData>("userPrivateData")

    val syncStatus = flow {
        emit(syncRepository.syncAccount(authUser))
    }

    suspend fun logOut() {
        authRepository.signOutUser()
    }

    suspend fun showWelcome() = authRepository.getShowWelcomePref()
}
