package com.puntogris.posture.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.lyft.kronos.AndroidClockFactory
import com.lyft.kronos.KronosClock
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.alarm.Notifications
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.db.AppDatabase
import com.puntogris.posture.data.datasource.local.db.ReminderDao
import com.puntogris.posture.data.datasource.local.db.UserDao
import com.puntogris.posture.data.datasource.remote.FirebaseDataSource
import com.puntogris.posture.data.datasource.remote.FirebaseRankingDataSource
import com.puntogris.posture.data.datasource.remote.FirebaseReminderDataSource
import com.puntogris.posture.data.datasource.remote.FirebaseUserDataSource
import com.puntogris.posture.data.repository.*
import com.puntogris.posture.domain.repository.*
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.StandardDispatchers
import com.puntogris.posture.utils.constants.Constants.ROOM_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun providesReminderDao(appDatabase: AppDatabase) = appDatabase.reminderDao()

    @Singleton
    @Provides
    fun providesUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Singleton
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

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = StandardDispatchers()

    @Singleton
    @Provides
    fun provideUserRepository(
        firebaseUser: FirebaseUserDataSource,
        alarm: Alarm,
        dispatchers: DispatcherProvider,
        appDatabase: AppDatabase
    ): UserRepository {
        return UserRepositoryImpl(firebaseUser, alarm, dispatchers, appDatabase)
    }

    @Provides
    fun provideTicketRepository(
        firebase: FirebaseDataSource,
        dispatchers: DispatcherProvider
    ): TicketRepository {
        return TicketRepositoryImpl(firebase, dispatchers)
    }

    @Singleton
    @Provides
    fun provideRankingRepository(
        rankingsFirebase: FirebaseRankingDataSource,
        dispatchers: DispatcherProvider
    ): RankingsRepository {
        return RankingsRepositoryImpl(rankingsFirebase, dispatchers)
    }

    @Provides
    fun provideSyncRepository(
        reminderDao: ReminderDao,
        userDao: UserDao,
        firestoreUser: FirebaseUserDataSource,
        firestoreReminder: FirebaseReminderDataSource,
        dataStore: DataStore,
        dispatchers: DispatcherProvider,
        workManager: WorkManager,
        kronosClock: KronosClock
    ): SyncRepository {
        return SyncRepositoryImpl(
            reminderDao,
            userDao,
            firestoreUser,
            firestoreReminder,
            dataStore,
            dispatchers,
            workManager,
            kronosClock
        )
    }

    @Singleton
    @Provides
    fun provideReminderRepository(
        firebase: FirebaseReminderDataSource,
        reminderDao: ReminderDao,
        notifications: Notifications,
        alarm: Alarm,
        dataStore: DataStore,
        dispatchers: DispatcherProvider,
        workManager: WorkManager,
    ): ReminderRepository {
        return ReminderRepositoryImpl(
            firebase, reminderDao, notifications, alarm, dataStore, dispatchers, workManager
        )
    }

    @Singleton
    @Provides
    fun provideDayLogsRepository(
        appDatabase: AppDatabase,
        dispatchers: DispatcherProvider
    ): DayLogsRepository {
        return DayLogsRepositoryImpl(
            appDatabase, dispatchers
        )
    }

    @Provides
    @Singleton
    fun provideKronosClock(@ApplicationContext context: Context): KronosClock {
        return AndroidClockFactory.createKronosClock(context)
    }

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager{
        return WorkManager.getInstance(context)
    }

}