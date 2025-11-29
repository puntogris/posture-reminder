package com.puntogris.posture.framework.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.puntogris.posture.domain.repository.SyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SyncAccountWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            syncRepository.syncAccountExperience()
            Result.success()
        } catch (t: Throwable) {
            Firebase.crashlytics.log("SyncAccountWorker")
            Firebase.crashlytics.recordException(t)
            Result.failure()
        }
    }

    companion object {
        const val SYNC_WORKER_NAME = "sync_experience_worker"
        const val SYNC_WORKER_NAME_DEPRECATED = "sync_account_worker"
    }
}
