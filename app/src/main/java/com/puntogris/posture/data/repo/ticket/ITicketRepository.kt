package com.puntogris.posture.data.repo.ticket

import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.model.Ticket

interface ITicketRepository {
    suspend fun fillTicketWithUserDataAndSend(ticket: Ticket): SimpleResult
}