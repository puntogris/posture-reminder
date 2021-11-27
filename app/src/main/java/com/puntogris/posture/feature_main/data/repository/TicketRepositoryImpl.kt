package com.puntogris.posture.feature_main.data.repository

import com.puntogris.posture.common.data.data_source.remote.FirebaseClients
import com.puntogris.posture.feature_main.domain.model.Ticket
import com.puntogris.posture.feature_main.domain.repository.TicketRepository
import com.puntogris.posture.common.utils.DispatcherProvider
import com.puntogris.posture.common.utils.Result
import com.puntogris.posture.common.utils.constants.Constants.TICKET_COLLECTION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class TicketRepositoryImpl(
    private val firebase: FirebaseClients,
    private val dispatchers: DispatcherProvider
) : TicketRepository {

    override fun sendTicket(ticket: Ticket): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading())

            val firebaseUser = firebase.getCurrentUser
            ticket.apply {
                username = firebaseUser?.displayName.toString()
                email = firebaseUser?.email.toString()
                uid = firebaseUser?.uid.toString()
            }

            firebase.firestore.collection(TICKET_COLLECTION).add(ticket).await()

            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error())
        }
    }.flowOn(dispatchers.io)
}
