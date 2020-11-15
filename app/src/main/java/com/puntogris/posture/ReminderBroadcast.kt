package com.puntogris.posture

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.puntogris.posture.di.HiltBroadcastReceiver
import com.puntogris.posture.preferences.SharedPref
import com.puntogris.posture.utils.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.Constants.POSTURE_NOTIFICATION_ID
import com.puntogris.posture.utils.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.Utils
import com.puntogris.posture.utils.millisToMinutes
import com.puntogris.posture.utils.setMidnight
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcast: HiltBroadcastReceiver() {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var alarm: Alarm

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == DAILY_ALARM_TRIGGERED){
            val intervalTime = sharedPref.getTimeIntervalForNotifications()
            alarm.startRepeatingAlarm(intervalTime!!.toInt())
        }
        else if(intent.action == REPEATING_ALARM_TRIGGERED) {
            val now = Calendar.getInstance()
            val midnight = Calendar.getInstance().apply {
                setMidnight()
            }
            val startTime = sharedPref.getStartTimePeriodForNotifications()
            val endTime = sharedPref.getEndTimePeriodForNotifications()

            val currentMinutesSinceMidnight= (now.timeInMillis - midnight.timeInMillis).millisToMinutes()

            //check if alarm goes beyond 0 am
            if (startTime < endTime){
                if(currentMinutesSinceMidnight <= sharedPref.getEndTimePeriodForNotifications()){
                    deliverNotification(context)
                }else alarm.cancelRepeatingAlarm()
            }else{
                if (currentMinutesSinceMidnight in (endTime + 1) until startTime){
                    alarm.cancelRepeatingAlarm()
                }else{
                    deliverNotification(context)
                }
            }
        }
    }

    private fun deliverNotification(context: Context){
        if (sharedPref.showNotificationStatus()) {
            val builder = NotificationCompat.Builder(context, POSTURE_NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_notifications_24px)
                .setContentTitle(context.getString(R.string.posture_notification_title))
                .setContentText(context.getString(R.string.posture_notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(100, builder.build())
            }
        }
    }


}