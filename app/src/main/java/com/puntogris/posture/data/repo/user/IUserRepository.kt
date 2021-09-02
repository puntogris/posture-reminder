package com.puntogris.posture.data.repo.user

import androidx.lifecycle.LiveData
import com.puntogris.posture.model.UserPrivateData
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUserFlowRoom(): Flow<UserPrivateData>
    fun getUserLiveDataRoom(): LiveData<UserPrivateData>
    suspend fun updateUsernameInRoomAndFirestore(name: String): Boolean
    suspend fun updateActiveReminderUserRoom(reminderId: String)
}