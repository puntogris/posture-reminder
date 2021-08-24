package com.puntogris.posture.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Keep
@Entity
data class DayLog(

    @PrimaryKey(autoGenerate = true)
    var dayLogId: Int = 0,

    @ColumnInfo
    var expGained: Int = 0,

    @ColumnInfo
    var notifications: Int = 0,

    @ColumnInfo
    var exercises: Int = 0,

    @ColumnInfo
    var date: String = LocalDate.now().toString()
)