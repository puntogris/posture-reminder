package com.puntogris.posture.data.datasource.remote

import com.puntogris.posture.data.datasource.toPublicProfile
import com.puntogris.posture.domain.model.Ticket
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.repository.UserServerApi
import com.puntogris.posture.utils.constants.Constants
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserApi @Inject constructor(
    private val firebase: FirebaseClients
) : UserServerApi {

    private fun privateProfileRef() = firebase.firestore
        .collection(Constants.USERS_COLLECTION)
        .document(requireNotNull(firebase.currentUid))

    private fun publicProfileRef() = privateProfileRef()
        .collection(Constants.PUBLIC_PROFILE_COL_GROUP)
        .document(Constants.PUBLIC_PROFILE_DOC)

    override suspend fun getUser(): UserPrivateData? {
        return privateProfileRef()
            .get()
            .await()
            .toObject(UserPrivateData::class.java)
    }

    override suspend fun insertUser(userPrivateData: UserPrivateData) {
        firebase.firestore.runBatch {
            it.set(privateProfileRef(), userPrivateData)
            it.set(publicProfileRef(), userPrivateData.toPublicProfile())
        }.await()
    }

    override suspend fun deleteAccount() {
        firebase.firestore.runBatch {
            it.delete(privateProfileRef())
            it.delete(publicProfileRef())
        }.await()
    }

    override suspend fun updateUsername(username: String) {
        firebase.firestore.runBatch {
            it.update(privateProfileRef(), Constants.USER_NAME_FIELD, username)
            it.update(publicProfileRef(), Constants.USER_NAME_FIELD, username)
        }.await()
    }

    override suspend fun updateExperience(experience: Int) {
        firebase.firestore.runBatch {
            it.update(privateProfileRef(), Constants.EXPERIENCE_FIELD, experience)
            it.update(publicProfileRef(), Constants.EXPERIENCE_FIELD, experience)
        }.await()
    }

    override suspend fun sendTicket(ticket: Ticket) {
        firebase.firestore
            .collection(Constants.TICKET_COLLECTION)
            .document(ticket.id)
            .set(ticket)
            .await()
    }
}