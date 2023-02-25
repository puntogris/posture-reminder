package com.puntogris.posture.utils

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

object PermissionsManager {

    fun needsPermissionForApp(context: Context): Boolean {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        val notificationManager = NotificationManagerCompat.from(context)
        val needsAlarmPermission = !canScheduleExactAlarmsCompat(alarmManager)
        val needsNotificationPermission = !notificationManager.areNotificationsEnabled()
        return needsNotificationPermission || needsAlarmPermission
    }

    fun needsAlarmPermission(context: Context): Boolean {
        val alarmManager = context.getSystemService(AlarmManager::class.java)
        return !canScheduleExactAlarmsCompat(alarmManager)
    }

    fun needsNotificationPermission(context: Context): Boolean {
        val notificationManager = NotificationManagerCompat.from(context)
        return !notificationManager.areNotificationsEnabled()
    }

    private fun canScheduleExactAlarmsCompat(alarmManager: AlarmManager): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}
