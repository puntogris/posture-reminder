package com.puntogris.posture.di

import android.content.Context
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.db.UserDao
import com.puntogris.posture.data.datasource.remote.FirebaseLoginDataSource
import com.puntogris.posture.data.datasource.remote.GoogleSingInDataSource
import com.puntogris.posture.data.repository.LoginRepositoryImpl
import com.puntogris.posture.domain.repository.LoginRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.StandardDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AuthModule {

    @Provides
    @Singleton
    fun provideGoogleSignInClient(@ApplicationContext context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    fun provideLoginRepository(
        workManager: WorkManager,
        loginFirebase: FirebaseLoginDataSource,
        dataStore: DataStore,
        userDao: UserDao,
        googleSingIn: GoogleSingInDataSource,
        alarm: Alarm,
        @ApplicationContext context: Context,
        dispatcherProvider: DispatcherProvider
    ): LoginRepository {
        return LoginRepositoryImpl(
            workManager,
            loginFirebase,
            dataStore,
            userDao,
            googleSingIn,
            alarm,
            context,
            dispatcherProvider
        )
    }
}