package com.puntogris.posture.utils

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.puntogris.posture.utils.Constants.POSTURE_NOTIFICATION_ID

object Utils {

    fun minutesFromMidnightToHourlyTime(minutes: Int): String {
        val hour: Int = minutes / 60
        val minute: Int = minutes % 60

        return String.format("%02d:%02d", hour, minute)
    }

    fun createNotificationChannel(activity: Activity) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notification posture"
            val descriptionText = "channel for posture notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(POSTURE_NOTIFICATION_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
}

