package com.puntogris.posture.data.repo.user

import androidx.lifecycle.LiveData
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.model.UserPrivateData
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUserFlowRoom(): Flow<UserPrivateData>
    fun getUserLiveDataRoom(): LiveData<UserPrivateData>
    suspend fun updateUsernameInRoomAndFirestore(name: String): SimpleResult
    suspend fun updateActiveReminderUserRoom(reminderId: String)
    suspend fun updateUserExperienceRoom(dayLog: DayLog): SimpleResult
}