package com.puntogris.posture

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.utils.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.DataStore
import com.puntogris.posture.utils.Utils.getTriggerTime
import com.puntogris.posture.utils.getHours
import com.puntogris.posture.utils.getMinutes
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class Alarm @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore
    ) {

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

    //todo
    //cambiar como funcionan las alarmas
    suspend fun startDailyAlarm(reminder: Reminder){
        val periodHour = reminder.startTime.getHours()
        val periodMin = reminder.endTime.getMinutes()

        println(periodHour)
        println(periodMin)

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
        dataStore.isCurrentReminderStateActive(true)
    }

    suspend fun cancelAlarms(){
        alarmManager.apply {
            cancel(pendingIntentDailyAlarm)
            cancel(pendingIntentRepeatingAlarm)
        }
        dataStore.isCurrentReminderStateActive(false)
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

    suspend fun refreshAlarms(reminder: Reminder){
        cancelAlarms()
        startDailyAlarm(reminder)

    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun canScheduleExactAlarms() = alarmManager.canScheduleExactAlarms()

}