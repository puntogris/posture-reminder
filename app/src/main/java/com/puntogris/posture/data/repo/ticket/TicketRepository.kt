package com.puntogris.posture.data.repo.ticket

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.model.Ticket
import com.puntogris.posture.utils.Constants.TICKET_COLLECTION_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class TicketRepository @Inject constructor(): ITicketRepository {

    private val firestore = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    private val firebaseUser = auth.currentUser

    override suspend fun fillTicketWithUserDataAndSend(ticket: Ticket): SimpleResult {
        ticket.apply {
            username = firebaseUser?.displayName.toString()
            email = firebaseUser?.email.toString()
            uid = firebaseUser?.uid.toString()
        }
        return sendTicketToFirestore(ticket)
    }

    private suspend fun sendTicketToFirestore(ticket: Ticket): SimpleResult = withContext(Dispatchers.IO){
        try {
            firestore.collection(TICKET_COLLECTION_NAME).add(ticket).await()
            SimpleResult.Success
        }catch (e: Exception){ SimpleResult.Failure }
    }
}