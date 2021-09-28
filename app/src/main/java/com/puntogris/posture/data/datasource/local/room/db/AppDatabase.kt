package com.puntogris.posture.data.datasource.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.puntogris.posture.data.datasource.local.room.dao.DayLogsDao
import com.puntogris.posture.data.datasource.local.room.dao.ReminderDao
import com.puntogris.posture.data.datasource.local.room.dao.UserDao
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.utils.Converters

@Database(entities = [
    Reminder::class,
    UserPrivateData::class,
    DayLog::class
                     ], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun userDao(): UserDao
    abstract fun dayLogsDao(): DayLogsDao
}