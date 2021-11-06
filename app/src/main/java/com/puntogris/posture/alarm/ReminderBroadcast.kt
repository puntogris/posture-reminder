package com.puntogris.posture.alarm

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.Context
import android.content.Intent
import android.os.Build
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.utils.Utils.dayOfTheWeek
import com.puntogris.posture.utils.Utils.minutesSinceMidnight
import com.puntogris.posture.utils.constants.Constants.DAILY_ALARM_TRIGGERED
import com.puntogris.posture.utils.constants.Constants.REPEATING_ALARM_TRIGGERED
import com.puntogris.posture.utils.goAsync
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
            REPEATING_ALARM_TRIGGERED -> onRepeatingAlarmTriggered()
            ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> onExactAlarmPermissionStateChanged()
        }
    }

    private fun onDailyAlarmTriggered() {
        goAsync {
            reminderRepository.getActiveReminder()?.apply {
                if (dayOfTheWeek() in alarmDays) alarm.startRepeatingAlarm(timeInterval)
            }
        }
    }

    private fun onRepeatingAlarmTriggered() {
        goAsync {
            reminderRepository.getActiveReminder()?.let {
                if (it.isAlarmInRange(minutesSinceMidnight())) deliverNotificationAndSetNewAlarm(it)
                else alarm.cancelRepeatingAlarm()
            }
        }
    }

    private fun onExactAlarmPermissionStateChanged() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            goAsync {
                reminderRepository.getActiveReminder()?.let {
                    alarm.setAlarmOnExactAlarmStateChange(it)
                }
            }
        }
    }

    private fun deliverNotificationAndSetNewAlarm(reminder: Reminder) {
        notifications.buildAndShowNotificationWithReminder(reminder)
        alarm.startRepeatingAlarm(reminder.timeInterval)
    }
}