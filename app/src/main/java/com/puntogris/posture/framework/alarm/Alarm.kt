package com.puntogris.posture.framework.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.Utils.getTriggerTime
import com.puntogris.posture.utils.constants.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.constants.Constants.REPEATING_ALARM_TRIGGERED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val DAILY_ALARM_REQUEST_CODE = 100
private const val REPEATING_ALARM_REQUEST_CODE = 200

class Alarm @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStoreHelper: DataStoreHelper
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val dailyAlarmIntent = Intent(context, ReminderBroadcast::class.java).apply {
        action = DAILY_ALARM_TRIGGERED
    }

    private val repeatingAlarmIntent = Intent(context, ReminderBroadcast::class.java).apply {
        action = REPEATING_ALARM_TRIGGERED
    }

    private val pendingIntentDailyAlarm = PendingIntent.getBroadcast(
        context,
        DAILY_ALARM_REQUEST_CODE,
        dailyAlarmIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    private val pendingIntentRepeatingAlarm = PendingIntent.getBroadcast(
        context,
        REPEATING_ALARM_REQUEST_CODE,
        repeatingAlarmIntent,
        PendingIntent.FLAG_IMMUTABLE
    )

    suspend fun startDailyAlarm(reminder: Reminder) {
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            reminder.triggerTimeAtMillis(),
            AlarmManager.INTERVAL_DAY,
            pendingIntentDailyAlarm
        )
        startRepeatingAlarm(reminder.timeInterval)
        dataStoreHelper.isCurrentReminderStateActive(true)
    }

    suspend fun cancelAlarms() {
        alarmManager.apply {
            cancel(pendingIntentDailyAlarm)
            cancel(pendingIntentRepeatingAlarm)
        }
        dataStoreHelper.isCurrentReminderStateActive(false)
    }

    fun startRepeatingAlarm(intervalInMinutes: Int) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            getTriggerTime(intervalInMinutes),
            pendingIntentRepeatingAlarm
        )
    }

    fun cancelRepeatingAlarm() {
        alarmManager.cancel(pendingIntentRepeatingAlarm)
    }

    suspend fun refreshAlarms(reminder: Reminder) {
        cancelAlarms()
        startDailyAlarm(reminder)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun canScheduleExactAlarms() = alarmManager.canScheduleExactAlarms()

    @RequiresApi(Build.VERSION_CODES.S)
    suspend fun setAlarmOnExactAlarmStateChange(reminder: Reminder) {
        if (canScheduleExactAlarms()) startDailyAlarm(reminder) else cancelAlarms()
    }
}
