package com.puntogris.posture.domain.repository

import com.puntogris.posture.domain.model.Ticket
import com.puntogris.posture.utils.SimpleResult

interface TicketRepository {
    suspend fun fillTicketWithUserDataAndSend(ticket: Ticket): SimpleResult
}