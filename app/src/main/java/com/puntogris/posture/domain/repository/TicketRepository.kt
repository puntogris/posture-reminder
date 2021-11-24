package com.puntogris.posture.domain.repository

import com.puntogris.posture.domain.model.Ticket
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    fun sendTicket(ticket: Ticket): Flow<Result<Unit>>
}