package com.puntogris.posture.utils

import com.maxkeppeler.sheets.options.Option
import java.util.*

object Utils {

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

    fun getTriggerTime(interval: Int) = Calendar.getInstance().timeInMillis + interval.minutesToMillis()

    fun getSavedOptionsArray(savedList: List<Int>?, daysList: Array<String>):Array<Option> {
        return daysList.mapIndexed { index, _ ->
            if (!savedList.isNullOrEmpty() && savedList.contains(index)){
                Option(daysList[index]).select()
            } else
                Option(daysList[index])
        }.toTypedArray()
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

