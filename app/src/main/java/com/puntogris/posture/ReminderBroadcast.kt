package com.puntogris.posture

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderBroadcast: BroadcastReceiver() {

    override fun onReceive(context: Context?, p1: Intent?) {

        val builder = NotificationCompat.Builder(context!!, "postureNotification")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Posture Notification")
            .setContentText("Time to fix that back posture!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(100, builder.build())
        }
    }
}