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
import com.puntogris.posture.data.datasource.remote.FirebaseClients
import com.puntogris.posture.data.datasource.remote.FirebaseReminderApi
import com.puntogris.posture.data.datasource.remote.FirebaseUserApi
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
    fun providesReminderDao(appDatabase: AppDatabase) = appDatabase.reminderDao

    @Singleton
    @Provides
    fun providesUserDao(appDatabase: AppDatabase) = appDatabase.userDao

    @Singleton
    @Provides
    fun providesDayHistoryDao(appDatabase: AppDatabase) = appDatabase.dayLogsDao

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
        firebaseClients: FirebaseClients,
        userServerApi: UserServerApi,
        alarm: Alarm,
        dispatchers: DispatcherProvider,
        appDatabase: AppDatabase
    ): UserRepository {
        return UserRepositoryImpl(firebaseClients, userServerApi, alarm, dispatchers, appDatabase)
    }

    @Provides
    fun provideTicketRepository(
        firebase: FirebaseClients,
        dispatchers: DispatcherProvider
    ): TicketRepository {
        return TicketRepositoryImpl(firebase, dispatchers)
    }

    @Singleton
    @Provides
    fun provideRankingRepository(
        firebaseClients: FirebaseClients,
        dispatchers: DispatcherProvider
    ): RankingsRepository {
        return RankingsRepositoryImpl(firebaseClients, dispatchers)
    }

    @Provides
    fun provideSyncRepository(
        firebaseClients: FirebaseClients,
        appDatabase: AppDatabase,
        firestoreUser: FirebaseUserApi,
        firestoreReminder: FirebaseReminderApi,
        dataStore: DataStore,
        dispatchers: DispatcherProvider,
        workManager: WorkManager,
        kronosClock: KronosClock
    ): SyncRepository {
        return SyncRepositoryImpl(
            firebaseClients,
            appDatabase,
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
        firebaseClients: FirebaseClients,
        firebase: FirebaseReminderApi,
        reminderDao: ReminderDao,
        notifications: Notifications,
        alarm: Alarm,
        dataStore: DataStore,
        dispatchers: DispatcherProvider,
        workManager: WorkManager,
    ): ReminderRepository {
        return ReminderRepositoryImpl(
            firebaseClients,
            firebase,
            reminderDao,
            notifications,
            alarm,
            dataStore,
            dispatchers,
            workManager
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
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideReminderServerApi(firebaseClients: FirebaseClients): ReminderServerApi {
        return FirebaseReminderApi(firebaseClients)
    }

    @Provides
    @Singleton
    fun provideUserServerApi(firebaseClients: FirebaseClients): UserServerApi {
        return FirebaseUserApi(firebaseClients)
    }
}