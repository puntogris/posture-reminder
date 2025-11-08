package com.puntogris.posture

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.work.Configuration
import com.jakewharton.threetenabp.AndroidThreeTen
import com.lyft.kronos.KronosClock
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.framework.alarm.Notifications
import com.puntogris.posture.framework.workers.SyncAccountWorker.Companion.SYNC_WORKER_NAME_DEPRECATED
import com.puntogris.posture.framework.workers.WorkersManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PostureApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workersManager: WorkersManager

    @Inject
    lateinit var notifications: Notifications

    @Inject
    lateinit var dataStoreHelper: DataStoreHelper

    @Inject
    lateinit var kronosClock: KronosClock

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        kronosClock.syncInBackground()

        runBlocking {
            dataStoreHelper.appTheme().let {
                AppCompatDelegate.setDefaultNightMode(it)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifications.removeDeprecatedChannels()
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        ProcessLifecycleOwner.get().lifecycleScope.launch {
            workersManager.cancelWorker(SYNC_WORKER_NAME_DEPRECATED)
        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder().setWorkerFactory(workerFactory).build()

}