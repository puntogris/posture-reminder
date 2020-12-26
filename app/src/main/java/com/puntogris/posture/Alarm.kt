package com.puntogris.posture

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.puntogris.posture.model.ReminderConfig
import com.puntogris.posture.utils.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.Utils.getTriggerTime
import com.puntogris.posture.utils.Utils.millisFromMidnightToHourlyTime
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
            0
    )

    private val pendingIntentRepeatingAlarm = PendingIntent.getBroadcast(
            context,
            200,
            repeatingAlarmIntent,
            0
    )

    fun startDailyAlarm(reminderConfig: ReminderConfig){
        val periodHour = reminderConfig.startTime.getHours()
        val periodMin = reminderConfig.endTime.getMinutes()

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

        Toast.makeText(context, context.getString(
            R.string.notifications_set_toast,
            millisFromMidnightToHourlyTime(reminderConfig.startTime),
            millisFromMidnightToHourlyTime(reminderConfig.endTime)),Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarms(){
        alarmManager.cancel(pendingIntentDailyAlarm)
        alarmManager.cancel(pendingIntentRepeatingAlarm)
    }

    fun startRepeatingAlarm(interval: Long){
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                getTriggerTime(interval),
                pendingIntentRepeatingAlarm
        )
    }

    fun cancelRepeatingAlarm() {
        alarmManager.cancel(pendingIntentRepeatingAlarm)
    }

}