package com.puntogris.posture.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maxkeppeler.bottomsheets.options.Option
import java.util.*

object Utils {
    private val gson = Gson()

    fun minutesFromMidnightToHourlyTime(minutes: Int) =
         String.format("%02d:%02d", minutes.getHours(), minutes.getMinutes())

    fun minutesSinceMidnight(): Int{
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return (now.timeInMillis - midnight.timeInMillis).millisToMinutes()
    }

    fun getTriggerTime(interval: Int) =
        Calendar.getInstance().timeInMillis + interval.minutesToMillis()

    fun getNotificationsPref(context: Context): Boolean{
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPref.getBoolean("pref_show_notifications", Constants.DEFAULT_SHOW_NOTIFICATIONS_PREF_VALUE)
    }

    fun getSavedOptionsArray(savedListString: String?, daysList: Array<String>):Array<Option> {
        val savedList = fromString(savedListString)
        return daysList.mapIndexed { index, _ ->
            if (!savedList.isNullOrEmpty() && savedList.contains(index)){
                Option(daysList[index]).select()
            } else
                Option(daysList[index])
        }.toTypedArray()
    }

    fun fromString(value: String?): List<Int> {
        val type = object: TypeToken<List<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    fun fromArrayList(list: List<Int>): String {
        val type = object: TypeToken<List<Int>>() {}.type
        return gson.toJson(list, type)
    }

    fun dayOfTheWeek(): Int{
        val calendar: Calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> 0
                    Calendar.TUESDAY -> 1
                    Calendar.WEDNESDAY -> 2
                    Calendar.THURSDAY -> 3
                    Calendar.FRIDAY -> 4
                    Calendar.SATURDAY -> 5
                    Calendar.SUNDAY -> 6
                    else -> 8
                }
    }
}

