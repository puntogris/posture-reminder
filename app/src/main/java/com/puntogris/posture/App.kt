package com.puntogris.posture

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.jakewharton.threetenabp.AndroidThreeTen
import com.puntogris.posture.utils.Constants
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App: Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        createNotificationChannel()
        setupSyncAccountWorkManager()
    }

    private fun setupSyncAccountWorkManager(){
        val syncWork = PeriodicWorkRequestBuilder<UploadWorker>(5, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("upload_work", ExistingPeriodicWorkPolicy.KEEP, syncWork)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                Constants.POSTURE_NOTIFICATION_ID,
                Constants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).also {
                it.description = Constants.CHANNEL_DESCRIPTION
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(it)
            }
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}