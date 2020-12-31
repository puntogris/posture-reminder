package com.puntogris.posture.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.model.RepoResult
import com.puntogris.posture.utils.Constants.BUG_REPORT_COLLECTION_NAME
import com.puntogris.posture.utils.Constants.REPORT_FIELD_FIRESTORE
import com.puntogris.posture.utils.Constants.TIMESTAMP_FIELD_FIRESTORE
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(): IRepository {

    private val firestore = Firebase.firestore

    override suspend fun sendReportToFirestore(message: String): RepoResult {
        val report = hashMapOf(
                REPORT_FIELD_FIRESTORE to message,
                TIMESTAMP_FIELD_FIRESTORE to Timestamp.now()
        )
        return try {
            firestore.collection(BUG_REPORT_COLLECTION_NAME).document().set(report).await()
            RepoResult.Success
        }catch (e:Exception){
            RepoResult.Failure
        }
    }

}
