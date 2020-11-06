package com.puntogris.posture.di

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.puntogris.posture.ReminderBroadcast
import dagger.hilt.android.qualifiers.ActivityContext
import java.util.*
import javax.inject.Inject

class Alarm @Inject constructor(@ActivityContext private val context:Context) {

    private val intent = Intent(context, ReminderBroadcast::class.java)
    private val pendingIntent= PendingIntent.getBroadcast(context, 0, intent, 0)
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun start(intervalInMins: Int, timePeriod: MutableSet<String>){
        val periodHour = timePeriod.first().toInt()
        val periodMin = timePeriod.last().toInt()

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 10)
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 2, pendingIntent )
    }

    fun end(){
        alarmManager.cancel(pendingIntent)
    }

}