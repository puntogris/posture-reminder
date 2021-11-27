package com.puntogris.posture.feature_main.domain.repository

import com.puntogris.posture.feature_main.domain.model.Ticket
import com.puntogris.posture.common.utils.Result
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    fun sendTicket(ticket: Ticket): Flow<Result<Unit>>
}