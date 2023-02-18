package com.puntogris.posture.utils

import com.maxkeppeler.sheets.options.Option
import com.puntogris.posture.utils.extensions.getHours
import com.puntogris.posture.utils.extensions.getMinutes
import com.puntogris.posture.utils.extensions.millisToMinutes
import com.puntogris.posture.utils.extensions.minutesToMillis
import java.util.Calendar
import java.util.Date

object Utils {

    fun minutesFromMidnightToHourlyTime(minutes: Int) =
        String.format("%02d:%02d", minutes.getHours(), minutes.getMinutes())

    fun minutesSinceMidnight(): Int {
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return (now.timeInMillis - midnight.timeInMillis).millisToMinutes()
    }

    fun getDateFromMinutesSinceMidnight(minutesSinceMidnight: Int): Date {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, minutesSinceMidnight.getHours())
            set(Calendar.MINUTE, minutesSinceMidnight.getMinutes())
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

    fun getTriggerTime(interval: Int) =
        Calendar.getInstance().timeInMillis + interval.minutesToMillis()

    fun getSavedOptions(savedList: List<Int>?, daysList: Array<String>): MutableList<Option> {
        return daysList.mapIndexed { index, _ ->
            Option(daysList[index]).apply {
                if (!savedList.isNullOrEmpty() && index in savedList) select()
            }
        }.toMutableList()
    }

    fun dayOfTheWeek(): Int {
        return when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
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

