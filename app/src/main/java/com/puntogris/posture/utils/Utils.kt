package com.puntogris.posture.utils

object Utils {

    fun minutesFromMidnightToHourlyTime(minutes: Int): String {
        val hour: Int = minutes / 60
        val minute: Int = minutes % 60

        return String.format("%02d:%02d", hour, minute)
    }
}

