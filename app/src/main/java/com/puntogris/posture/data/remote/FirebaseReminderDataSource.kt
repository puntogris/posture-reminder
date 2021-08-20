package com.puntogris.posture.data.remote

import javax.inject.Inject

class FirebaseReminderDataSource @Inject constructor() : FirebaseDataSource() {

    fun getReminderDocumentRefWithId(reminderId: String) =
        firestore.collection("users")
            .document(getCurrentUserId())
            .collection("reminders")
            .document(reminderId)

    fun getNewReminderDocumentRef() =
        firestore.collection("users")
            .document(getCurrentUserId())
            .collection("reminders")
            .document()

    fun getUserRemindersQuery() =
        firestore
            .collection("users")
            .document(getCurrentUserId())
            .collection("reminders")
            .limit(10)

}