package com.puntogris.posture

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.di.HiltBroadcastReceiver
import com.puntogris.posture.utils.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.Constants.POSTURE_NOTIFICATION_ID
import com.puntogris.posture.utils.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.SharedPref
import com.puntogris.posture.utils.Utils.dayOfTheWeek
import com.puntogris.posture.utils.Utils.minutesSinceMidnight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcast: HiltBroadcastReceiver() {

    @Inject lateinit var alarm: Alarm
    @Inject lateinit var reminderDao: ReminderDao
    @Inject lateinit var sharedPref: SharedPref

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == DAILY_ALARM_TRIGGERED){
            GlobalScope.launch {
                reminderDao.getReminderConfig().apply {
                    if (dayOfTheWeek() in alarmDays) alarm.startRepeatingAlarm(timeInterval)
                }
            }
        }
        else if(intent.action == REPEATING_ALARM_TRIGGERED) {
            GlobalScope.launch {
                val minutesSinceMidnight = minutesSinceMidnight()
                reminderDao.getReminderConfig().apply {
                    if (alarmNotPastMidnight()){
                        if(minutesSinceMidnight <= endTime)
                            deliverNotificationAndSetNewAlarm(context, timeInterval)
                        else
                            alarm.cancelRepeatingAlarm()
                    }else{
                        if (alarmPastMidnightAndOutOfRange(minutesSinceMidnight))
                            alarm.cancelRepeatingAlarm()
                        else
                            deliverNotificationAndSetNewAlarm(context, timeInterval)
                    }
                }
            }
        }
    }

    private fun deliverNotificationAndSetNewAlarm(context: Context, timeInterval: Int){
        if (sharedPref.getNotificationsPref()) {
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