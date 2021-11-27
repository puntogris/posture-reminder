package com.puntogris.posture.feature_main.data.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.puntogris.posture.feature_main.domain.model.DayLog
import com.puntogris.posture.feature_main.domain.model.Reminder
import com.puntogris.posture.feature_main.domain.model.UserPrivateData
import com.puntogris.posture.common.utils.constants.Converters

@Database(
    entities = [
        Reminder::class,
        UserPrivateData::class,
        DayLog::class
    ], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val reminderDao: ReminderDao
    abstract val userDao: UserDao
    abstract val dayLogsDao: DayLogsDao
}