package com.puntogris.posture

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.puntogris.posture.data.repo.reminder.ReminderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val reminderRepository: ReminderRepository
):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        try {
            println("reminderworker")
            val reminderId = inputData.getString("reminderId")
            if (!reminderId.isNullOrBlank()) {
                println("data not null")
                reminderRepository.insertReminderIntoFirestoreFromRoom(reminderId)
            }
            Result.success()
        }catch (e:Exception){
            Result.failure()
        }
    }
}