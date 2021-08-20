package com.puntogris.posture.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.utils.Constants.APP_PREFERENCES_NAME
import com.puntogris.posture.utils.Constants.APP_THEME
import com.puntogris.posture.utils.Constants.LAST_VERSION_CODE
import com.puntogris.posture.utils.Constants.PANDA_ANIMATION
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_PREFERENCES_NAME)

    suspend fun lastVersionCode() =
        context.dataStore.data.first()[intPreferencesKey(LAST_VERSION_CODE)] ?: BuildConfig.VERSION_CODE

    suspend fun updateLastVersionCode() = context.dataStore.edit {
        it[intPreferencesKey(LAST_VERSION_CODE)] = BuildConfig.VERSION_CODE
    }

    suspend fun appTheme() = context.dataStore.data.first()[intPreferencesKey(APP_THEME)] ?: 2

    fun appThemeFlow() = context.dataStore.data.map {
        val value = it[intPreferencesKey(APP_THEME)] ?: 2
        if (value == -1) 2 else value - 1
    }


    fun appThemeFlow2() = context.dataStore.data.map {
        it[intPreferencesKey(APP_THEME)] ?: 2
    }

    suspend fun setAppTheme(value: Int) = context.dataStore.edit {
        it[intPreferencesKey(APP_THEME)] = value
    }

    suspend fun setPandaAnimation(value:Boolean){
        context.dataStore.edit {
            it[booleanPreferencesKey(PANDA_ANIMATION)] = value
        }
    }

    fun currentReminderState() = context.dataStore.data.map {
        it[booleanPreferencesKey("a")] ?: false
    }

    suspend fun setCurrentReminderState(value: Boolean){
        context.dataStore.edit {
            it[booleanPreferencesKey("a")] = value
        }
    }

    suspend fun toggleIsCurrentReminderState(){
        context.dataStore.edit {
            it[booleanPreferencesKey("a")] = !(it[booleanPreferencesKey("a")] ?: false)
        }
    }

}