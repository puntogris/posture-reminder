package com.puntogris.posture.feature_main.domain.repository

import com.puntogris.posture.feature_auth.domain.model.UserPrivateData

interface UserServerApi {
    suspend fun getUser(): UserPrivateData?

    suspend fun insertUser(userPrivateData: UserPrivateData)

    suspend fun deleteAccount()

    suspend fun updateUsername(username: String)

    suspend fun updateExperience(experience: Int)
}