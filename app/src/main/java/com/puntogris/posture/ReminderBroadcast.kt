package com.puntogris.posture

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.puntogris.posture.data.repo.reminder.ReminderRepository
import com.puntogris.posture.utils.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.Utils.dayOfTheWeek
import com.puntogris.posture.utils.Utils.minutesSinceMidnight
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

    @Inject
    lateinit var notifications: Notifications

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            DAILY_ALARM_TRIGGERED -> onDailyAlarmTriggered()
            REPEATING_ALARM_TRIGGERED -> onRepeatingAlarmTriggered(context)
            ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> onExactAlarmPermissionStateChanged()
        }
    }

    private fun onDailyAlarmTriggered(){
        goAsync {
            reminderRepository.getActiveReminder()?.apply {
                if (dayOfTheWeek() in alarmDays) alarm.startRepeatingAlarm(timeInterval)
            }
        }
    }

    private fun onRepeatingAlarmTriggered(context: Context){
        goAsync {
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
    }

    private fun onExactAlarmPermissionStateChanged(){
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

    private fun deliverNotificationAndSetNewAlarm(context: Context, timeInterval: Int) {
        goAsync {
            reminderRepository.getActiveReminder()?.let {
                val nb = notifications.getNotificationBuilderWithReminder(it)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notifications.createChannelForReminderSdkO(it)
                }

                with(NotificationManagerCompat.from(context)) {
                    notify(it.id.hashCode(), nb.build())
                }
            }
        }

        alarm.startRepeatingAlarm(timeInterval)
    }

}