package com.puntogris.posture

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.puntogris.posture.utils.getHours
import com.puntogris.posture.utils.getMinutes
import dagger.hilt.android.qualifiers.ActivityContext
import java.util.*
import javax.inject.Inject

class Alarm @Inject constructor(@ActivityContext private val context: Context) {

    private val intent = Intent(context, ReminderBroadcast::class.java)
    private val pendingIntent= PendingIntent.getBroadcast(context, 0, intent, 0)

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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
            pendingIntent
        )
    }

    fun start(intervalInMins: Long, startTimePeriod: Int, endTimePeriod: Int){
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
            1000 * 60 * intervalInMins,
            pendingIntent
        )
    }

    fun end(){
        alarmManager.cancel(pendingIntent)
    }

}