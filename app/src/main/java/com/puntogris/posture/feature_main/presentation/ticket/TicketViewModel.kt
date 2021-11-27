package com.puntogris.posture.feature_main.presentation.ticket

import android.text.Editable
import androidx.lifecycle.ViewModel
import com.puntogris.posture.feature_main.domain.model.Ticket
import com.puntogris.posture.feature_main.domain.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private var ticket = Ticket()

    fun sendTicket() = ticketRepository.sendTicket(ticket)

    fun updateTicketType(type: Int) {
        ticket.type = if (type == 0) Ticket.PROBLEM_TYPE else Ticket.SUGGESTION_TYPE
    }

    fun updateMessage(editable: Editable) {
        ticket.message = editable.toString()
    }
}