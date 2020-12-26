package com.puntogris.posture.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.utils.Constants
import com.puntogris.posture.utils.Constants.BUG_REPORT_COLLECTION_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(): IRepository {

    private val firestore = Firebase.firestore

    override fun sendReportToFirestore(message: String) {
        firestore.collection(BUG_REPORT_COLLECTION_NAME).document().set("message" to "report")
            .addOnFailureListener {
                println(it.localizedMessage)
            }
            .addOnSuccessListener {
                println("yay")
            }
    }

}