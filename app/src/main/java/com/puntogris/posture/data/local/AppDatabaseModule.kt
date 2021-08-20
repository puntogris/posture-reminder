package com.puntogris.posture.data.local

import android.content.Context
import androidx.room.Room
import com.puntogris.posture.utils.Constants.ROOM_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppDatabaseModule {

    @Provides
    fun providesReminderDao(appDatabase: AppDatabase) = appDatabase.reminderDao()

    @Provides
    fun providesUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Provides
    fun providesDayHistoryDao(appDatabase: AppDatabase) = appDatabase.dayLogsDao()

    @Provides
    @Singleton
    fun provideReminderDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room
            .databaseBuilder(
                appContext,
                AppDatabase::class.java,
                ROOM_DATABASE_NAME
            )
            .build()
    }
}