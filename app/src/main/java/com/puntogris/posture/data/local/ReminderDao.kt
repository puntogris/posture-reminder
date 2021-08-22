package com.puntogris.posture.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.puntogris.posture.model.Reminder

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: Reminder):Long

    @Update
    suspend fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)

    @Query("SELECT * FROM reminder WHERE id = :reminderId")
    suspend fun getReminderWithId(reminderId: String): Reminder?

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM reminder r INNER JOIN UserPrivateData u ON currentReminderId = r.id")
    suspend fun getActiveReminder(): Reminder?

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM reminder r INNER JOIN UserPrivateData u ON currentReminderId = r.id")
    fun getActiveReminderLiveData(): LiveData<Reminder?>

    @Query("SELECT * FROM reminder")
    fun getAllRemindersLiveData(): LiveData<List<Reminder>>

    @Query("SELECT id FROM reminder")
    suspend fun getAllRemindersIds(): List<String>

    @Transaction
    suspend fun insertRemindersIfNotInRoom(firestoreReminders: List<Reminder>){
        val roomRemindersIds = getAllRemindersIds()
        firestoreReminders.forEach {
            if (it.id in roomRemindersIds){
                insert(it)
            }
        }
    }

}