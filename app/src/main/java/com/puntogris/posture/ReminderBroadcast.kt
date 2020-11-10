package com.puntogris.posture

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.puntogris.posture.utils.millisToMinutes
import com.puntogris.posture.utils.setMidnight
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.time.hours

@AndroidEntryPoint
class ReminderBroadcast: HiltBroadcastReceiver() {

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var alarm: Alarm

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == "DAILY_ALARM_TRIGGERED"){
            val intervalTime = sharedPref.getTimeIntervalForRepeatingAlarm()
            alarm.startRepeatingAlarm(intervalTime!!.toInt())
        }
        else if(intent.action == "REPEATING_ALARM_TRIGGERED") {
            val now = Calendar.getInstance()
            val midnight = Calendar.getInstance().apply {
                setMidnight()
            }

            val currentMinutesSinceMidnight= (now.timeInMillis - midnight.timeInMillis).millisToMinutes()
            if(currentMinutesSinceMidnight <= sharedPref.getEndTimePeriodForAlarm()){
               deliverNotification(context)
            }else alarm.cancelRepeatingAlarm()
        }
    }

    private fun deliverNotification(context: Context){
        if (sharedPref.showNotificationStatus()) {
            val builder = NotificationCompat.Builder(context, "postureNotification")
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(context.getString(R.string.posture_notification_title))
                .setContentText(context.getString(R.string.posture_notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(100, builder.build())
            }
        }
    }


}