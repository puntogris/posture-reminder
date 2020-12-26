package com.puntogris.posture.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.maxkeppeler.bottomsheets.options.Option
import java.util.*

object Utils {

    fun minutesFromMidnightToHourlyTime(minutes: Int) =
         String.format("%02d:%02d", minutes.getHours(), minutes.getMinutes())


    fun minutesSinceMidnight(): Int{
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            setMidnight()
        }
        return (now.timeInMillis - midnight.timeInMillis).millisToMinutes()
    }

    fun getTriggerTime(interval: Int) =
        Calendar.getInstance().timeInMillis + interval.minutesToMillis()

    fun getNotificationsPref(context: Context): Boolean{
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getBoolean("pref_show_notifications", Constants.DEFAULT_SHOW_NOTIFICATIONS_PREF_VALUE)
    }

    fun getSavedOptionsArray(savedList: List<Int>?, daysList: Array<String>):Array<Option> =
        daysList.mapIndexed { index, _ ->
            if (!savedList.isNullOrEmpty() && savedList.contains(index)){
                Option(daysList[index]).select()
            } else
                Option(daysList[index])
        }.toTypedArray()

}

