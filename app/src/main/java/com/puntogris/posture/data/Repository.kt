package com.puntogris.posture.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.utils.Constants
import javax.inject.Inject

class Repository @Inject constructor(): IRepository {

    private val firestore = Firebase.firestore

    override fun sendReportToFirestore(message: String) {
        firestore.collection(Constants.BUG_REPORT_COLLECTION_NAME).document()
            .set(message)
    }

}