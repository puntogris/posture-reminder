package com.puntogris.posture

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.repo.reminder.ReminderRepository
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
    lateinit var reminderRepository: ReminderRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            DAILY_ALARM_TRIGGERED -> goAsync {
                reminderRepository.getActiveReminder()?.apply {
                    if (dayOfTheWeek() in alarmDays) alarm.startRepeatingAlarm(timeInterval)
                }
            }
            REPEATING_ALARM_TRIGGERED -> goAsync {
                val minutesSinceMidnight = minutesSinceMidnight()
                reminderRepository.getActiveReminder()?.apply {
                    if (alarmNotPastMidnight()) {
                        if (minutesSinceMidnight <= endTime)
                            deliverNotificationAndSetNewAlarm(context, timeInterval)
                        else
                            alarm.cancelRepeatingAlarm()
                    } else {
                        if (alarmPastMidnightAndOutOfRange(minutesSinceMidnight))
                            alarm.cancelRepeatingAlarm()
                        else
                            deliverNotificationAndSetNewAlarm(context, timeInterval)
                    }
                }
            }
            ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarm.canScheduleExactAlarms()){
                        goAsync {
                            reminderRepository.getActiveReminder()?.let {
                                alarm.startDailyAlarm(it)
                            }
                        }
                    }else alarm.cancelAlarms()
                }
            }
        }
    }

    private fun phoneIsInNormalMode(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL
    }

    private fun deliverNotificationAndSetNewAlarm(context: Context, timeInterval: Int) {
        goAsync {
            val reminder = reminderRepository.getActiveReminder()

            val claimExpIntent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.navigation)
                .setDestination(R.id.claimNotificationExpDialog)
                .createPendingIntent()


            val builder = NotificationCompat.Builder(context, POSTURE_NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_app_logo)
                .setContentTitle(context.getString(R.string.posture_notification_title))
                .setContentText(context.getString(R.string.posture_notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(claimExpIntent)
                .addAction(R.drawable.ic_baseline_home_24, "Reclama tu exp", claimExpIntent)

            reminder?.let {
                if (it.soundUri.isNotBlank() && phoneIsInNormalMode(context))
                    builder.setSound(Uri.parse(it.soundUri))
                if (it.vibrationPattern != 0) builder.setVibrate(vibrationPatterns[it.vibrationPattern])
            }

            with(NotificationManagerCompat.from(context)) {
                notify(100, builder.build())
            }

            alarm.startRepeatingAlarm(timeInterval)
        }
    }
}