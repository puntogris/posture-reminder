package com.puntogris.posture

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.jakewharton.threetenabp.AndroidThreeTen
import com.puntogris.posture.alarm.Notifications
import com.puntogris.posture.utils.Constants.SYNC_ACCOUNT_WORKER
import com.puntogris.posture.utils.DataStore
import com.puntogris.posture.workers.SyncAccountWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    @Inject
    lateinit var notifications: Notifications
    @Inject
    lateinit var dataStore: DataStore

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        applyAppTheme()
        removeDeprecatedNotificationChannels()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun applyAppTheme() {
        val theme = runBlocking { dataStore.appTheme() }
        AppCompatDelegate.setDefaultNightMode(theme)
    }

    private fun removeDeprecatedNotificationChannels() {
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