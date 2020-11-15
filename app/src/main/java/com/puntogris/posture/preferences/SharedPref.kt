package com.puntogris.posture.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import com.puntogris.posture.Alarm
import com.puntogris.posture.R
import com.puntogris.posture.utils.Constants.DEFAULT_APP_STATUS_PREF_VALUE
import com.puntogris.posture.utils.Constants.DEFAULT_END_TIME_PERIOD_NOTIFICATIONS_PREF_VALUE
import com.puntogris.posture.utils.Constants.DEFAULT_INTERVAL_REPEATING_NOTIFICATIONS_PREF_VALUE
import com.puntogris.posture.utils.Constants.DEFAULT_PANDA_ANIMATION_VALUE
import com.puntogris.posture.utils.Constants.DEFAULT_SHOW_NOTIFICATIONS_PREF_VALUE
import com.puntogris.posture.utils.Constants.DEFAULT_START_TIME_PERIOD_NOTIFICATIONS_PREF_VALUE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext private val context:Context, private val alarm: Alarm) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    private fun getAppStatus() = sharedPref.getBoolean("app_status", DEFAULT_APP_STATUS_PREF_VALUE)

    fun appStatusText() = if (getAppStatus()) context.getString(R.string.stop_alarm) else context.getString(
        R.string.start_alarm
    )

    fun appStatusSummaryText()= if(getAppStatus()) context.getString(R.string.alarm_on) else context.getString(
        R.string.alarm_off
    )

    fun changeAppStatus(): String{
        val isAlarmActive = getAppStatus()
        sharedPref.edit().putBoolean("app_status", !isAlarmActive).apply()
        if (isAlarmActive) alarm.cancelAlarms()
        else alarm.startDailyAlarm(getStartTimePeriodForNotifications())

        return appStatusText()
    }

    // the alarm will go on every X minutes
    fun getTimeIntervalForNotifications() =
        sharedPref.getString("time_interval_for_notifications", DEFAULT_INTERVAL_REPEATING_NOTIFICATIONS_PREF_VALUE)

    // time to start the alarm
    fun getStartTimePeriodForNotifications()=
        sharedPref.getInt("pref_start_notification_time", DEFAULT_START_TIME_PERIOD_NOTIFICATIONS_PREF_VALUE)

    // time to end the alarm
    fun getEndTimePeriodForNotifications()=
            sharedPref.getInt("pref_end_notification_time", DEFAULT_END_TIME_PERIOD_NOTIFICATIONS_PREF_VALUE)

    fun showNotificationStatus()=
        sharedPref.getBoolean("pref_show_notifications", DEFAULT_SHOW_NOTIFICATIONS_PREF_VALUE)

    fun getShowPandaAnimation() =
        sharedPref.getBoolean("panda_animation", DEFAULT_PANDA_ANIMATION_VALUE)

}