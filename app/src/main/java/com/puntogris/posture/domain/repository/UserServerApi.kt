package com.puntogris.posture.domain.repository

import com.puntogris.posture.domain.model.Ticket
import com.puntogris.posture.domain.model.UserPrivateData

interface UserServerApi {

    suspend fun getUser(): UserPrivateData?

    suspend fun insertUser(userPrivateData: UserPrivateData)

    suspend fun deleteAccount()

    suspend fun updateUsername(username: String)

    suspend fun updateExperience(experience: Int)

    suspend fun sendTicket(ticket: Ticket)
}
