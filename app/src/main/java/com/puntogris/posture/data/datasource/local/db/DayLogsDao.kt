package com.puntogris.posture.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.puntogris.posture.domain.model.DayLog
import kotlinx.coroutines.flow.Flow

@Dao
interface DayLogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayLog: DayLog)

    @Update
    suspend fun update(dayLog: DayLog)

    @Query("SELECT * from DayLog ORDER BY dateId DESC LIMIT 1")
    suspend fun getLastEntry(): DayLog?

    @Query("SELECT * from DayLog WHERE dateId >= (SELECT date('now', '-2 day', 'localtime')) ORDER BY dateId DESC LIMIT 2")
    fun getLastTwoEntries(): Flow<List<DayLog>>

    @Query("SELECT * from DayLog WHERE dateId >= (SELECT date('now', '-7 day', 'localtime')) ORDER BY dateId DESC LIMIT 7")
    fun getWeekEntries(): Flow<List<DayLog>>

    @Query("SELECT * from DayLog WHERE dateId = date('now', 'localtime')")
    suspend fun getTodayLog(): DayLog?
}