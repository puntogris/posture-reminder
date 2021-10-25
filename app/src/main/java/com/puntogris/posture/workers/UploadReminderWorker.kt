package com.puntogris.posture.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.puntogris.posture.data.repository.reminder.ReminderRepository
import com.puntogris.posture.utils.Constants.REMINDER_ID_WORKER_DATA
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class UploadReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val reminderRepository: ReminderRepository
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try {
            val reminderId = inputData.getString(REMINDER_ID_WORKER_DATA)

            if (!reminderId.isNullOrBlank()) {
                reminderRepository.insertLocalReminderToServer(reminderId)
            }
            Result.success()
        }catch (e:Exception){
            Result.failure()
        }
    }
}