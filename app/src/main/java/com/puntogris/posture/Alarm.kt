package com.puntogris.posture

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.puntogris.posture.utils.getHours
import com.puntogris.posture.utils.getMinutes
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class Alarm @Inject constructor(@ApplicationContext private val context: Context) {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val dailyAlarmIntent = Intent(context, ReminderBroadcast::class.java).apply {
        action = "DAILY_ALARM_TRIGGERED"
    }

    private val repeatingAlarmIntent = Intent(context, ReminderBroadcast::class.java).apply {
        action = "REPEATING_ALARM_TRIGGERED"
    }

    private val pendingIntentDailyAlarm = PendingIntent.getBroadcast(
            context,
            100,
            dailyAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val pendingIntentRepeatingAlarm = PendingIntent.getBroadcast(
            context,
            200,
            repeatingAlarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    fun startDailyAlarm(startTimePeriod: Int){
        val periodHour = startTimePeriod.getHours()
        val periodMin = startTimePeriod.getMinutes()

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, periodHour)
            set(Calendar.MINUTE, periodMin)
        }
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntentDailyAlarm
        )
    }

    fun cancelAlarms(){
        alarmManager.cancel(pendingIntentDailyAlarm)
        alarmManager.cancel(pendingIntentRepeatingAlarm)
    }

    fun startRepeatingAlarm(intervalInMinutes: Int){
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                intervalInMinutes * 60 * 1000.toLong(),
                pendingIntentRepeatingAlarm
        )
    }

    fun cancelRepeatingAlarm() {
        alarmManager.cancel(pendingIntentRepeatingAlarm)
    }

}