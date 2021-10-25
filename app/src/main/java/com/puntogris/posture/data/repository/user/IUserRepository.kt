package com.puntogris.posture.data.repository.user

import androidx.lifecycle.LiveData
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.utils.SimpleResult

interface IUserRepository {
    fun getLocalUserLiveData(): LiveData<UserPrivateData>
    suspend fun getLocalUser(): UserPrivateData?
    suspend fun updateLocalAndServerUsername(name: String): SimpleResult
    suspend fun updateLocalActiveReminder(reminder: Reminder)
    fun isUserLoggedIn(): Boolean
}