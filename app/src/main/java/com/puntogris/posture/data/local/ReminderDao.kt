package com.puntogris.posture.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.puntogris.posture.model.ReminderConfig
import kotlinx.coroutines.flow.Flow
import java.lang.reflect.Array
import kotlin.collections.ArrayList

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminderConfig: ReminderConfig)

    @Update
    suspend fun update(reminderConfig: ReminderConfig)

    @Query("SELECT * FROM reminderconfig WHERE id = 1")
    fun getReminderConfigLiveData(): LiveData<ReminderConfig>

    @Query("SELECT * FROM reminderconfig WHERE id = 1")
    fun getReminderConfig(): ReminderConfig

    @Query("UPDATE reminderconfig SET startTime = :startTime WHERE id = 1")
    suspend fun updateStartTime(startTime: Int)

    @Query("UPDATE reminderconfig SET endTime = :endTime WHERE id = 1")
    suspend fun updateEndTime(endTime: Int)

    @Query("UPDATE reminderconfig SET timeInterval = :timeInterval WHERE id = 1")
    suspend fun updateTimeInterval(timeInterval: Int)

    @Query("UPDATE reminderconfig SET isActive = :isActive WHERE id = 1")
    suspend fun updateReminderStatus(isActive: Boolean)

    @Query("UPDATE reminderconfig SET alarmDays = :days WHERE id = 1")
    suspend fun updateAlarmDays(days: List<Int>)

    @Query("UPDATE reminderconfig SET showPanda = 1 WHERE id = 1")
    suspend fun enablePandaAnimation()
}