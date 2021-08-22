package com.puntogris.posture.data.remote

import com.puntogris.posture.utils.Constants.REMINDERS_COLLECTION
import com.puntogris.posture.utils.Constants.USERS_COLLECTION
import javax.inject.Inject

class FirebaseReminderDataSource @Inject constructor() : FirebaseDataSource() {

    fun getReminderDocumentRefWithId(reminderId: String) =
        firestore.collection(USERS_COLLECTION)
            .document(getCurrentUserId())
            .collection(REMINDERS_COLLECTION)
            .document(reminderId)

    fun getNewReminderDocumentRef() =
        firestore.collection(USERS_COLLECTION)
            .document(getCurrentUserId())
            .collection(REMINDERS_COLLECTION)
            .document()

    fun getUserRemindersQuery() =
        firestore
            .collection(USERS_COLLECTION)
            .document(getCurrentUserId())
            .collection(REMINDERS_COLLECTION)
            .limit(10)

}