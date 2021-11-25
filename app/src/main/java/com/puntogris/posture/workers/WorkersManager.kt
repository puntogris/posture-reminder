package com.puntogris.posture.workers

import androidx.work.*
import com.puntogris.posture.utils.constants.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

class WorkersManager @Inject constructor(
    private val workManager: WorkManager
) {

    suspend fun launchUploadReminderWorker(reminderId: String) {
        val reminderData =
            Data.Builder().putString(Constants.REMINDER_ID_WORKER_DATA, reminderId).build()
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val uploadReminder = OneTimeWorkRequestBuilder<UploadReminderWorker>()
            .setInputData(reminderData)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(uploadReminder).await()
    }

    suspend fun launchSyncAccountWorker() {
        val syncWork = PeriodicWorkRequestBuilder<SyncAccountWorker>(5, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            Constants.SYNC_ACCOUNT_WORKER,
            ExistingPeriodicWorkPolicy.KEEP,
            syncWork
        ).await()
    }

    suspend fun cancelWorker(worker: String) {
        workManager.cancelUniqueWork(worker).await()
    }
}