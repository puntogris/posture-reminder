package com.puntogris.posture

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.utils.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.Utils.getTriggerTime
import com.puntogris.posture.utils.getHours
import com.puntogris.posture.utils.getMinutes
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class Alarm @Inject constructor(@ApplicationContext private val context: Context) {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val dailyAlarmIntent = Intent(context, ReminderBroadcast::class.java).apply {
        action = DAILY_ALARM_TRIGGERED
    }

    private val repeatingAlarmIntent = Intent(context, ReminderBroadcast::class.java).apply {
        action = REPEATING_ALARM_TRIGGERED
    }

    private val pendingIntentDailyAlarm = PendingIntent.getBroadcast(
            context,
            100,
            dailyAlarmIntent,
            PendingIntent.FLAG_IMMUTABLE
    )

    private val pendingIntentRepeatingAlarm = PendingIntent.getBroadcast(
            context,
            200,
            repeatingAlarmIntent,
            PendingIntent.FLAG_IMMUTABLE

    )

    fun startDailyAlarm(reminder: Reminder){
        val periodHour = reminder.startTime.getHours()
        val periodMin = reminder.endTime.getMinutes()

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, periodHour)
            set(Calendar.MINUTE, periodMin)
        }
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntentDailyAlarm
        )
    }

    fun cancelAlarms(){
        alarmManager.apply {
            cancel(pendingIntentDailyAlarm)
            cancel(pendingIntentRepeatingAlarm)
        }
    }

    fun startRepeatingAlarm(intervalInMinutes: Int){
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                getTriggerTime(intervalInMinutes),
                pendingIntentRepeatingAlarm
        )
    }

    fun cancelRepeatingAlarm() {
        alarmManager.cancel(pendingIntentRepeatingAlarm)
    }

}