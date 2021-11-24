package com.puntogris.posture.domain.repository

import androidx.lifecycle.LiveData
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.utils.SimpleResult

interface UserRepository {

    fun isUserLoggedIn(): Boolean

    fun getLocalUserLiveData(): LiveData<UserPrivateData>

    suspend fun getLocalUser(): UserPrivateData?

    suspend fun updateUsername(name: String): SimpleResult

    suspend fun updateActiveReminder(reminder: Reminder)

    suspend fun deleteUserAccountData(): SimpleResult
}