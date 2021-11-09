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
import com.puntogris.posture.data.datasource.remote.*
import com.puntogris.posture.data.repository.AuthRepositoryImpl
import com.puntogris.posture.domain.repository.AuthRepository
import com.puntogris.posture.utils.DispatcherProvider
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
        authServerApi: AuthServerApi,
        dataStore: DataStore,
        userDao: UserDao,
        googleSingInApi: GoogleSingInApi,
        alarm: Alarm,
        dispatcherProvider: DispatcherProvider
    ): AuthRepository {
        return AuthRepositoryImpl(
            workManager,
            authServerApi,
            dataStore,
            userDao,
            googleSingInApi,
            alarm,
            dispatcherProvider
        )
    }

    @Provides
    @Singleton
    fun provideAuthServerApi(firebaseClients: FirebaseClients): AuthServerApi {
        return FirebaseAuthApi(firebaseClients)
    }
}