package com.puntogris.posture.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.puntogris.posture.data.Converters
import com.puntogris.posture.utils.Constants.DEFAULT_END_TIME_PERIOD
import com.puntogris.posture.utils.Constants.DEFAULT_INTERVAL_REPEATING
import com.puntogris.posture.utils.Constants.DEFAULT_START_TIME_PERIOD
import com.puntogris.posture.utils.getHours
import com.puntogris.posture.utils.millisToMinutes

@Entity
class ReminderConfig(

    @PrimaryKey
    val id: Int = 1,

    @ColumnInfo
    val isActive: Boolean = false,

    @ColumnInfo
    val timeInterval: Int = DEFAULT_INTERVAL_REPEATING,

    @ColumnInfo
    val startTime: Int = DEFAULT_START_TIME_PERIOD,

    @ColumnInfo
    val endTime: Int = DEFAULT_END_TIME_PERIOD,

    @ColumnInfo
    val showPanda: Boolean = false,

    @ColumnInfo
    val alarmDays: List<Int> = listOf(0,1,2,3,4,5)
){
    fun alarmPastMidnight() = startTime < endTime

    fun alarmDaysSummary(daysList: Array<String>) =
        alarmDays.mapIndexed { index, _ -> daysList[index].subSequence(0,3) }.joinToString(", ")

    fun timeIntervalSummary() =
        when{
            timeInterval < 60 -> "$timeInterval mins."
            timeInterval == 60 -> "1 h."
            else -> "${timeInterval.getHours()} hs."
        }
}