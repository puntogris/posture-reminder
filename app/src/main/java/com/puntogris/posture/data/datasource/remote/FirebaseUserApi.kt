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
        val updateData = mapOf(
            Constants.USER_NAME_FIELD to username,
            Constants.CLIENT_APP_VERSION_FIELD to ALLOWED_VERSION_TO_UPDATE
        )
        firebase.firestore.runBatch {
            it.update(privateProfileRef(), updateData)
            it.update(publicProfileRef(), updateData)
        }.await()
    }

    override suspend fun updateExperience(experience: Int) {
        val updateData = mapOf(
            Constants.EXPERIENCE_FIELD to experience,
            Constants.CLIENT_APP_VERSION_FIELD to ALLOWED_VERSION_TO_UPDATE
        )
        firebase.firestore.runBatch {
            it.update(privateProfileRef(), updateData)
            it.update(publicProfileRef(), updateData)
        }.await()
    }

    override suspend fun sendTicket(ticket: Ticket) {
        firebase.firestore
            .collection(Constants.TICKET_COLLECTION)
            .document(ticket.id)
            .set(ticket)
            .await()
    }

    companion object {
        // As a temporary fix we are not allowing updates without this version code
        // This will completely disable the periodic updates for all app installed
        private const val ALLOWED_VERSION_TO_UPDATE = 2
    }
}
