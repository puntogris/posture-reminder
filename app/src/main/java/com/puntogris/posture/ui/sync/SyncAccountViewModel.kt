package com.puntogris.posture.ui.sync

import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.repository.LoginRepository
import com.puntogris.posture.domain.repository.SyncRepository
import com.puntogris.posture.model.UserPrivateData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SyncAccountViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    private val loginRepository: LoginRepository
):ViewModel() {

    suspend fun synAccountWith(userPrivateData: UserPrivateData) =
        syncRepository.syncSeverAccountWithLocalDb(userPrivateData)

    suspend fun logOut(){
        loginRepository.signOutUser()
    }

    suspend fun showLogin() = loginRepository.getShowLoginPref()
}