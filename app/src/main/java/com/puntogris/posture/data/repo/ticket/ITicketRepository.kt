package com.puntogris.posture.data.repo.ticket

import com.puntogris.posture.model.Ticket
import com.puntogris.posture.utils.SimpleResult

interface ITicketRepository {
    suspend fun fillTicketWithUserDataAndSend(ticket: Ticket): SimpleResult
}