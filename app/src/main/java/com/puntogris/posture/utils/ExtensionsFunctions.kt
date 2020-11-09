package com.puntogris.posture.utils

import java.util.*

fun Int.getHours()= this / 60

fun Int.getMinutes()= this % 60

fun Long.millisToMinutes() = this / 1000 / 60

fun Calendar.setMidnight(){
    this.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

