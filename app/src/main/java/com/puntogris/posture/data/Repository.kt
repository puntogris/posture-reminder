package com.puntogris.posture.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.utils.Constants.BUG_REPORT_COLLECTION_NAME
import com.puntogris.posture.utils.Constants.REPORT_FIELD_FIRESTORE
import com.puntogris.posture.utils.Constants.REPORT_FIELD_TIMESTAMP
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class Repository @Inject constructor(): IRepository {

    private val firestore = Firebase.firestore

    override fun sendReportToFirestore(message: String) {
        val report = hashMapOf(
                REPORT_FIELD_FIRESTORE to message,
                REPORT_FIELD_TIMESTAMP to Timestamp.now()
        )
        firestore.collection(BUG_REPORT_COLLECTION_NAME).document().set(report)
                .addOnSuccessListener { }
                .addOnFailureListener { }
    }

}
