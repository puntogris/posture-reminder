package com.puntogris.posture.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class ReminderModule {
    @Provides
    fun providesLocationDao(reminderDatabase: ReminderDatabase): ReminderDao {
        return reminderDatabase.reminderDao()
    }

    @Provides
    @Singleton
    fun provideReminderDatabase(@ApplicationContext appContext: Context): ReminderDatabase {
        return Room
            .databaseBuilder(
                appContext,
                ReminderDatabase::class.java,
                "reminder_table"
            )
                .createFromAsset("databases/reminder_config.db")
            .build()
    }

}