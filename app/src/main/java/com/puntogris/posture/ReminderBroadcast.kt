package com.puntogris.posture

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ForegroundInfo
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.utils.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.Constants.POSTURE_NOTIFICATION_ID
import com.puntogris.posture.utils.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.DataStore
import com.puntogris.posture.utils.Utils.dayOfTheWeek
import com.puntogris.posture.utils.Utils.minutesSinceMidnight
import com.puntogris.posture.utils.Utils.vibrationPatterns
import com.puntogris.posture.utils.goAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@DelicateCoroutinesApi
@AndroidEntryPoint
class ReminderBroadcast : HiltBroadcastReceiver() {

    @Inject
    lateinit var alarm: Alarm
    @Inject
    lateinit var reminderDao: ReminderDao
    @Inject
    lateinit var dataStore: DataStore

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
//            DAILY_ALARM_TRIGGERED -> goAsync {
//                val reminder = reminderDao.getActiveReminder()
//                if (dayOfTheWeek() in reminder.alarmDays) alarm.startRepeatingAlarm(reminder.timeInterval)
//
//                reminderDao.getActiveReminder().apply {
//                    if (dayOfTheWeek() in alarmDays) alarm.startRepeatingAlarm(timeInterval)
//                }
            }
//            REPEATING_ALARM_TRIGGERED -> goAsync {
//                val minutesSinceMidnight = minutesSinceMidnight()
//                reminderDao.getActiveReminder().apply {
//                    if (alarmNotPastMidnight()) {
//                        if (minutesSinceMidnight <= endTime)
//                            deliverNotificationAndSetNewAlarm(context, timeInterval)
//                        else
//                            alarm.cancelRepeatingAlarm()
//                    } else {
//                        if (alarmPastMidnightAndOutOfRange(minutesSinceMidnight))
//                            alarm.cancelRepeatingAlarm()
//                        else
//                            deliverNotificationAndSetNewAlarm(context, timeInterval)
//                    }
//                }
//            }
//        }
    }

    private fun phoneIsInNormalMode(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL
    }

  //  private fun deliverNotificationAndSetNewAlarm(context: Context, timeInterval: Int) {
 //       goAsync {
//            val reminder = reminderDao.getActiveReminder()
//
//            val builder = NotificationCompat.Builder(context, POSTURE_NOTIFICATION_ID)
//                .setSmallIcon(R.drawable.ic_notifications_24px)
//                .setContentTitle(context.getString(R.string.posture_notification_title))
//                .setContentText(context.getString(R.string.posture_notification_text))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//            reminder.let {
//                if (it.soundUri.isNotBlank() && phoneIsInNormalMode(context)) builder.setSound(
//                    Uri.parse(it.soundUri)
//                )
//                if (it.vibrationPattern != 0) builder.setVibrate(vibrationPatterns[it.vibrationPattern])
//            }
//
//            with(NotificationManagerCompat.from(context)) {
//                notify(100, builder.build())
//            }
//
//            alarm.startRepeatingAlarm(timeInterval)
 //       }
 //   }
}