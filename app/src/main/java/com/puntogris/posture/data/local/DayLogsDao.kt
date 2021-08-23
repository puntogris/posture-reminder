package com.puntogris.posture.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.puntogris.posture.model.DayLog

@Dao
interface DayLogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayLog: DayLog)

    @Update
    suspend fun update(dayLog: DayLog)

    @Query("SELECT * from DayLog ORDER BY date DESC LIMIT 1")
    suspend fun getLastEntry(): DayLog?

    @Query("SELECT * from DayLog WHERE date >= (SELECT date('now', '-2 day')) ORDER BY date DESC LIMIT 2")
    fun getLastTwoEntries(): LiveData<List<DayLog>>

    @Query("SELECT * from DayLog WHERE date >= (SELECT date('now', '-7 day')) ORDER BY date DESC LIMIT 7")
    suspend fun getWeekEntries(): List<DayLog>

    @Query("SELECT * from DayLog WHERE date == date('now')")
    suspend fun getTodayLog(): DayLog?

}