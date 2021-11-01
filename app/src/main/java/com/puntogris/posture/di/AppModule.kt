package com.puntogris.posture.di

import android.content.Context
import androidx.room.Room
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.alarm.Notifications
import com.puntogris.posture.data.DispatcherProvider
import com.puntogris.posture.data.StandardDispatchers
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.room.dao.ReminderDao
import com.puntogris.posture.data.datasource.local.room.dao.UserDao
import com.puntogris.posture.data.datasource.local.room.db.AppDatabase
import com.puntogris.posture.data.datasource.remote.*
import com.puntogris.posture.data.repository.day_logs.DayLogsRepository
import com.puntogris.posture.data.repository.day_logs.DayLogsRepositoryImpl
import com.puntogris.posture.data.repository.login.LoginRepository
import com.puntogris.posture.data.repository.login.LoginRepositoryImpl
import com.puntogris.posture.data.repository.rankings.RankingsRepository
import com.puntogris.posture.data.repository.rankings.RankingsRepositoryImpl
import com.puntogris.posture.data.repository.reminder.ReminderRepository
import com.puntogris.posture.data.repository.reminder.ReminderRepositoryImpl
import com.puntogris.posture.data.repository.sync.SyncRepository
import com.puntogris.posture.data.repository.sync.SyncRepositoryImpl
import com.puntogris.posture.data.repository.ticket.TicketRepository
import com.puntogris.posture.data.repository.ticket.TicketRepositoryImpl
import com.puntogris.posture.data.repository.user.UserRepository
import com.puntogris.posture.data.repository.user.UserRepositoryImpl
import com.puntogris.posture.utils.Constants.ROOM_DATABASE_NAME
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
        @ApplicationContext context: Context
    ): SyncRepository {
        return SyncRepositoryImpl(
            reminderDao, userDao, firestoreUser, firestoreReminder, dataStore, dispatchers, context
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
        @ApplicationContext context: Context
    ): ReminderRepository {
        return ReminderRepositoryImpl(
            firebase, reminderDao, notifications, alarm, dataStore, dispatchers, context
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
    fun provideLoginRepository(
        @ApplicationContext context: Context,
        loginFirebase: FirebaseLoginDataSource,
        dataStore: DataStore,
        userDao: UserDao,
        googleSingIn: GoogleSingInDataSource,
        alarm: Alarm
    ): LoginRepository {
        return LoginRepositoryImpl(
            context, loginFirebase, dataStore, userDao, googleSingIn, alarm
        )
    }
}