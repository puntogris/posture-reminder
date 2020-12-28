package com.puntogris.posture

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.puntogris.posture.data.ReminderDao
import com.puntogris.posture.di.HiltBroadcastReceiver
import com.puntogris.posture.utils.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.Constants.POSTURE_NOTIFICATION_ID
import com.puntogris.posture.utils.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.Utils.dayOfTheWeek
import com.puntogris.posture.utils.Utils.fromString
import com.puntogris.posture.utils.Utils.getNotificationsPref
import com.puntogris.posture.utils.Utils.minutesSinceMidnight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcast : HiltBroadcastReceiver() {

    @Inject lateinit var alarm: Alarm
    @Inject lateinit var reminderDao: ReminderDao

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == DAILY_ALARM_TRIGGERED){
            GlobalScope.launch {
                reminderDao.getReminderConfig().apply {
                    val days = fromString(alarmDays)
                    if (dayOfTheWeek() in days) alarm.startRepeatingAlarm(timeInterval)
                }
            }
        }
        else if(intent.action == REPEATING_ALARM_TRIGGERED) {
            GlobalScope.launch {
                val currentMinutesSinceMidnight = minutesSinceMidnight()
                reminderDao.getReminderConfig().apply {
                    if (alarmPastMidnight()){
                        if(currentMinutesSinceMidnight <= endTime){
                            deliverNotificationAndSetNewAlarm(context, timeInterval)
                        }else alarm.cancelRepeatingAlarm()
                    }else{
                        if (currentMinutesSinceMidnight in (endTime + 1) until startTime){
                            alarm.cancelRepeatingAlarm()
                        }else{
                            deliverNotificationAndSetNewAlarm(context, timeInterval)
                        }
                    }
                }
            }
        }
    }

    private fun deliverNotificationAndSetNewAlarm(context: Context, timeInterval: Int){
        val showNotifications = getNotificationsPref(context)
        if (showNotifications) {
            val builder = NotificationCompat.Builder(context, POSTURE_NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle(context.getString(R.string.posture_notification_title))
                .setContentText(context.getString(R.string.posture_notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(100, builder.build())
            }
        }
        alarm.startRepeatingAlarm(timeInterval)
    }
}