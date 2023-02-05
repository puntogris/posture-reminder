package com.puntogris.posture.data.datasource.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import com.puntogris.posture.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: Reminder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminders: List<Reminder>)

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("SELECT * FROM reminder WHERE reminderId = :reminderId")
    suspend fun getReminderWithId(reminderId: String): Reminder?

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM reminder INNER JOIN UserPrivateData ON currentReminderId = reminderId")
    suspend fun getActiveReminder(): Reminder?

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM reminder r INNER JOIN UserPrivateData ON currentReminderId = reminderId")
    fun getActiveReminderStream(): Flow<Reminder?>

    @Query("SELECT * FROM reminder r INNER JOIN UserPrivateData u ON r.uid = u.uid ORDER BY name ASC")
    fun getAllRemindersLiveData(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminder WHERE uid = ''")
    suspend fun getRemindersNotSynced(): List<Reminder>

    @Query("SELECT reminderId FROM reminder")
    suspend fun getAllRemindersIds(): List<String>

    @Transaction
    suspend fun insertIfNotInRoom(firestoreReminders: List<Reminder>) {
        val roomRemindersIds = getAllRemindersIds()
        firestoreReminders.forEach {
            if (it.reminderId !in roomRemindersIds) insert(it)
        }
    }
}