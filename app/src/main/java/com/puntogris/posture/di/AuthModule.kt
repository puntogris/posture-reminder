package com.puntogris.posture.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.common.alarm.Alarm
import com.puntogris.posture.feature_main.data.datasource.local.DataStore
import com.puntogris.posture.feature_auth.data.data_source.FirebaseAuthApi
import com.puntogris.posture.feature_main.data.datasource.remote.FirebaseClients
import com.puntogris.posture.feature_main.data.datasource.remote.GoogleSingInApi
import com.puntogris.posture.feature_auth.data.repository.AuthRepositoryImpl
import com.puntogris.posture.feature_auth.domain.repository.AuthRepository
import com.puntogris.posture.feature_auth.domain.repository.AuthServerApi
import com.puntogris.posture.common.utils.DispatcherProvider
import com.puntogris.posture.common.workers.WorkersManager
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
        workersManager: WorkersManager,
        authServerApi: AuthServerApi,
        dataStore: DataStore,
        googleSingInApi: GoogleSingInApi,
        alarm: Alarm,
        dispatcherProvider: DispatcherProvider
    ): AuthRepository {
        return AuthRepositoryImpl(
            workersManager,
            authServerApi,
            dataStore,
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