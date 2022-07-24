package com.puntogris.posture

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.jakewharton.threetenabp.AndroidThreeTen
import com.lyft.kronos.KronosClock
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.framework.alarm.Notifications
import com.puntogris.posture.framework.workers.WorkersManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
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

        /*
        Forcing account sync because in v2.1.0 work manager wasn't working and new accounts created
        on that version won't have sync on, remove when all users migrate to 2.1.1+
         */
        if (BuildConfig.VERSION_CODE < 44) {
            MainScope().launch {
                workersManager.launchSyncAccountWorker()
            }
        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder().setWorkerFactory(workerFactory).build()

}