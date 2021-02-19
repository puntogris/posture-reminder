package com.puntogris.posture.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.model.Report
import com.puntogris.posture.utils.RepoResult
import com.puntogris.posture.utils.Constants.BUG_REPORT_COLLECTION_NAME
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(): IRepository {

    private val firestore = Firebase.firestore

    override suspend fun sendReportToFirestore(report: Report): RepoResult {
        return try {
            firestore.collection(BUG_REPORT_COLLECTION_NAME).document().set(report).await()
            RepoResult.Success
        }catch (e:Exception){
            RepoResult.Failure
        }
    }

}
