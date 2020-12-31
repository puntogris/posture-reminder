package com.puntogris.posture.model

import androidx.room.*
import com.puntogris.posture.utils.Constants.DEFAULT_END_TIME_PERIOD
import com.puntogris.posture.utils.Constants.DEFAULT_INTERVAL_REPEATING
import com.puntogris.posture.utils.Constants.DEFAULT_START_TIME_PERIOD
import com.puntogris.posture.utils.getHours
import org.jetbrains.annotations.NotNull

@Entity
data class ReminderConfig(

    @PrimaryKey
    val id: Int = 1,

    @ColumnInfo @NotNull
    val isActive: Boolean = false,

    @ColumnInfo@NotNull
    val timeInterval: Int = DEFAULT_INTERVAL_REPEATING,

    @ColumnInfo @NotNull
    val startTime: Int = DEFAULT_START_TIME_PERIOD,

    @ColumnInfo
    val endTime: Int = DEFAULT_END_TIME_PERIOD,

    @ColumnInfo
    val showPanda: Boolean = false,

    @ColumnInfo
    val alarmDays: List<Int> = listOf(0,1,2,3,4,5,6)
){
    fun alarmNotPastMidnight() = startTime < endTime

    fun alarmDaysSummary(daysList: Array<String>) =
         alarmDays.joinToString(", ") {
            daysList[it].subSequence(0, 3)
         }

    fun timeIntervalSummary() =
        when{
            timeInterval < 60 -> "$timeInterval mins."
            timeInterval == 60 -> "1 h."
            else -> "${timeInterval.getHours()} hs."
        }

    fun alarmPastMidnightAndOutOfRange(minutesSinceMidnight: Int) =
       minutesSinceMidnight in (endTime + 1) until startTime

}