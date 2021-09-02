package com.puntogris.posture

import android.app.Application
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.jakewharton.threetenabp.AndroidThreeTen
import com.puntogris.posture.alarm.Notifications
import com.puntogris.posture.utils.Constants.SYNC_ACCOUNT_WORKER
import com.puntogris.posture.workers.SyncAccountWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App: Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var notifications: Notifications

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        setupSyncAccountWorkManager()
        removeDeprecatedNotificationChannels()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun setupSyncAccountWorkManager(){
        val syncWork = PeriodicWorkRequestBuilder<SyncAccountWorker>(5, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(SYNC_ACCOUNT_WORKER, ExistingPeriodicWorkPolicy.KEEP, syncWork)
    }

    private fun removeDeprecatedNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifications.removeDeprecatedChannels()
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}