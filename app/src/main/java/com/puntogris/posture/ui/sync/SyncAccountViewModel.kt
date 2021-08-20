package com.puntogris.posture.ui.sync

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repo.sync.SyncRepository
import com.puntogris.posture.model.UserPrivateData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SyncAccountViewModel @Inject constructor(
    private val syncRepository: SyncRepository
):ViewModel() {

    suspend fun synAccountWith(userPrivateData: UserPrivateData) =
        syncRepository.syncFirestoreAccountWithRoom(userPrivateData)
}