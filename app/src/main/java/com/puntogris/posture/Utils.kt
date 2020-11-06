package com.puntogris.posture

object Utils {

    fun minutesFromMidnightToHourlyTime(minutes: Int): String {
        val hour: Int = minutes / 60
        val minute: Int = minutes % 60

        // Formatting here is more for adding "0" in front of single digit numbers,
        // so that e.g it writes out the hour format as "01:05" instead of "1:5"
        // See https://stackoverflow.com/a/27857435
        return String.format("%02d:%02d", hour, minute)
    }
}