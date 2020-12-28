package com.puntogris.posture.di

import androidx.room.Database
import androidx.room.RoomDatabase
import com.puntogris.posture.data.ReminderDao
import com.puntogris.posture.model.ReminderConfig

@Database(entities = [ReminderConfig::class], version = 1, exportSchema = true)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

}