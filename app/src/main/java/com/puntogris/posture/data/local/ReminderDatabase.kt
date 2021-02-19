package com.puntogris.posture.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.model.ReminderConfig
import com.puntogris.posture.utils.Converters

@Database(entities = [ReminderConfig::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ReminderDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}