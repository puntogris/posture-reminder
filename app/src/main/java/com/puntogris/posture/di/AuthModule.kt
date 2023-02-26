package com.puntogris.posture.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.data.datasource.remote.FirebaseAuthApi
import com.puntogris.posture.data.datasource.remote.FirebaseClients
import com.puntogris.posture.data.datasource.remote.GoogleSingInApi
import com.puntogris.posture.data.repository.AuthRepositoryImpl
import com.puntogris.posture.domain.repository.AuthRepository
import com.puntogris.posture.domain.repository.AuthServerApi
import com.puntogris.posture.framework.alarm.Alarm
import com.puntogris.posture.framework.workers.WorkersManager
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
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .build()
        return GoogleSignIn.getClient(context, options)
    }

    @Provides
    fun provideLoginRepository(
        workersManager: WorkersManager,
        authServerApi: AuthServerApi,
        dataStoreHelper: DataStoreHelper,
        googleSingInApi: GoogleSingInApi,
        alarm: Alarm,
        dispatcherProvider: DispatcherProvider
    ): AuthRepository {
        return AuthRepositoryImpl(
            workersManager,
            authServerApi,
            dataStoreHelper,
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
