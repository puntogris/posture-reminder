package com.puntogris.posture.data.datasource.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.puntogris.posture.domain.model.DayLog

@Dao
interface DayLogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayLog: DayLog)

    @Update
    suspend fun update(dayLog: DayLog)

    @Query("SELECT * from DayLog ORDER BY dateId DESC LIMIT 1")
    suspend fun getLastEntry(): DayLog?

    @Query("SELECT * from DayLog WHERE dateId >= (SELECT date('now', '-2 day', 'localtime')) ORDER BY dateId DESC LIMIT 2")
    fun getLastTwoEntries(): LiveData<List<DayLog>>

    @Query("SELECT * from DayLog WHERE dateId >= (SELECT date('now', '-7 day', 'localtime')) ORDER BY dateId DESC LIMIT 7")
    fun getWeekEntries(): LiveData<List<DayLog>>

    @Query("SELECT * from DayLog WHERE dateId = date('now', 'localtime')")
    suspend fun getTodayLog(): DayLog?
}