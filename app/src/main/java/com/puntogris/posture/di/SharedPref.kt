package com.puntogris.posture.di

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ActivityContext private val context:Context, private val alarm: Alarm) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    private val defaultAppStatus = true
    private val defaultTimePeriodForAlarm = setOf("12","12")
    private val defaultIntervalForRepeatingAlarm = "60"

    private fun getAppStatus() = sharedPref.getBoolean("app_status", defaultAppStatus)

    fun appStatusText() = if (getAppStatus()) "Stop" else "Start"

    fun changeAppStatus(): String{
        val isAlarmActive = getAppStatus()
        sharedPref.edit().putBoolean("app_status", !isAlarmActive).apply()
        if (isAlarmActive) alarm.end()
        else alarm.start(
                getTimeIntervalForRepeatingAlarm()!!.toInt(),
                getTimePeriodForAlarm() as MutableSet<String>)

        return appStatusText()
    }

    // the alarm will go on every X minutes
    private fun getTimeIntervalForRepeatingAlarm() =
        sharedPref.getString("time_interval_for_alarm", defaultIntervalForRepeatingAlarm)

    // set of initial and final time in which to trigger the alarm
    private fun getTimePeriodForAlarm()=
        sharedPref.getStringSet("time_period_for_alarm", defaultTimePeriodForAlarm)

}