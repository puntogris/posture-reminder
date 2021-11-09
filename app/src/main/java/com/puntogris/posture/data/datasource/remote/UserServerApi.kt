package com.puntogris.posture.data.datasource.remote

import com.puntogris.posture.domain.model.UserPrivateData

interface UserServerApi {
    suspend fun getUser(): UserPrivateData?
    suspend fun createUser(user: UserPrivateData)
    suspend fun deleteAccount()
    suspend fun updateUsername(username: String)
    suspend fun updateExperience(experience: Int)
}