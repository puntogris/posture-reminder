package com.puntogris.posture.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.puntogris.posture.data.Converters
import com.puntogris.posture.data.ReminderDao
import com.puntogris.posture.model.ReminderConfig

@Database(entities = [ReminderConfig::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

}