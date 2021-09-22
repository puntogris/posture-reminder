package com.puntogris.posture.utils

import android.content.Context
import androidx.annotation.Keep
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.utils.Constants.APP_PREFERENCES_NAME
import com.puntogris.posture.utils.Constants.APP_THEME
import com.puntogris.posture.utils.Constants.LAST_VERSION_CODE
import com.puntogris.posture.utils.Constants.LOGIN_COMPLETED_KEY
import com.puntogris.posture.utils.Constants.PANDA_ANIMATION
import com.puntogris.posture.utils.Constants.REMINDER_STATE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Keep
@Singleton
class DataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_PREFERENCES_NAME)

    suspend fun lastVersionCode() =
        context.dataStore.data.first()[intPreferencesKey(LAST_VERSION_CODE)] ?: 0

    suspend fun updateLastVersionCode() = context.dataStore.edit {
        it[intPreferencesKey(LAST_VERSION_CODE)] = BuildConfig.VERSION_CODE
    }

    suspend fun appTheme() = context.dataStore.data.first()[intPreferencesKey(APP_THEME)] ?: 1

    fun appThemeFlow() = context.dataStore.data.map {
        val value = it[intPreferencesKey(APP_THEME)] ?: 1
        if (value == -1) 2 else value - 1
    }

    suspend fun setAppTheme(value: Int) = context.dataStore.edit {
        it[intPreferencesKey(APP_THEME)] = value
    }

    suspend fun setPandaAnimation(value:Boolean){
        context.dataStore.edit {
            it[booleanPreferencesKey(PANDA_ANIMATION)] = value
        }
    }

    suspend fun showPandaAnimation() =
        context.dataStore.data.first()[booleanPreferencesKey(PANDA_ANIMATION)] ?: false

    fun alarmStatus() = context.dataStore.data.map {
        it[booleanPreferencesKey(REMINDER_STATE_KEY)] ?: false
    }

    suspend fun isCurrentReminderStateActive(value: Boolean){
        context.dataStore.edit {
            it[booleanPreferencesKey(REMINDER_STATE_KEY)] = value
        }
    }

    suspend fun isLoginCompleted() =
        context.dataStore.data.first()[booleanPreferencesKey(LOGIN_COMPLETED_KEY)] ?: false


    suspend fun setLoginCompletedPref(value: Boolean) = context.dataStore.edit {
        it[booleanPreferencesKey(LOGIN_COMPLETED_KEY)] = value
    }
}