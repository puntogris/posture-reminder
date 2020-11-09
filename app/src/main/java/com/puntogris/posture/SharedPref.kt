package com.puntogris.posture

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext private val context:Context, private val alarm: Alarm) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    private val defaultAppStatus = false
    // 720 are minutes after midnight, so 12am
    private val defaultStartTimePeriodForAlarm = 720
    // 0 are minutes after midnight, so 12pm
    private val defaultEndTimePeriodForAlarm = 0
    private val defaultIntervalForRepeatingAlarm = "60"
    private val defaultShowNotificationStatus = true


    private fun getAppStatus() = sharedPref.getBoolean("app_status", defaultAppStatus)

    fun appStatusText() = if (getAppStatus()) "Stop" else "Start"

    fun appStatusSummaryText()= if(getAppStatus()) "The alarm is on" else "The alarm is off"


    fun changeAppStatus(): String{
        val isAlarmActive = getAppStatus()
        sharedPref.edit().putBoolean("app_status", !isAlarmActive).apply()
        if (isAlarmActive) alarm.cancelAlarms()
        else alarm.startDailyAlarm(getStartTimePeriodForAlarm())

        return appStatusText()
    }

    // the alarm will go on every X minutes
    fun getTimeIntervalForRepeatingAlarm() =
        sharedPref.getString("time_interval_for_alarm", defaultIntervalForRepeatingAlarm)

    // time to start the alarm
    private fun getStartTimePeriodForAlarm()=
        sharedPref.getInt("pref_start_notification_time", defaultStartTimePeriodForAlarm)

    // time to end the alarm
    fun getEndTimePeriodForAlarm()=
            sharedPref.getInt("pref_end_notification_time", defaultEndTimePeriodForAlarm)

    fun showNotificationStatus()=
        sharedPref.getBoolean("pref_show_notifications", defaultShowNotificationStatus)


}