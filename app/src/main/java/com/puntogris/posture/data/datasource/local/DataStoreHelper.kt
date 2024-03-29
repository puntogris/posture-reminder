package com.puntogris.posture.data.datasource.local

import android.content.Context
import androidx.annotation.Keep
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_TO_DISABLE_CHECKPOINT
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_TO_ENABLE_CHECKPOINT
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_TO_RETRY_CHECKPOINT
import com.puntogris.posture.utils.constants.Keys
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Keep
@Singleton
class DataStoreHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = Keys.APP_PREFERENCES_NAME)

    suspend fun lastVersionCode() =
        context.dataStore.data.first()[intPreferencesKey(Keys.LAST_VERSION_CODE)] ?: 0

    suspend fun updateLastVersionCode() = context.dataStore.edit {
        it[intPreferencesKey(Keys.LAST_VERSION_CODE)] = BuildConfig.VERSION_CODE
    }

    suspend fun appTheme() = context.dataStore.data.first()[intPreferencesKey(Keys.APP_THEME)] ?: 1

    fun appThemeFlow() = context.dataStore.data.map {
        val value = it[intPreferencesKey(Keys.APP_THEME)] ?: 1
        if (value == -1) 2 else value - 1
    }

    suspend fun setAppTheme(value: Int) = context.dataStore.edit {
        it[intPreferencesKey(Keys.APP_THEME)] = value
    }

    suspend fun setPandaAnimation(value: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey(Keys.PANDA_ANIMATION)] = value
        }
    }

    fun showPandaAnimation() = context.dataStore.data.map {
        it[booleanPreferencesKey(Keys.PANDA_ANIMATION)] ?: false
    }

    fun isAlarmActiveFlow() = context.dataStore.data.map {
        it[booleanPreferencesKey(Keys.REMINDER_STATE_KEY)] ?: false
    }

    suspend fun isAlarmActive() = isAlarmActiveFlow().first()

    suspend fun isCurrentReminderStateActive(value: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey(Keys.REMINDER_STATE_KEY)] = value
        }
    }

    suspend fun showLoginPref() =
        context.dataStore.data.first()[booleanPreferencesKey(Keys.SHOW_LOGIN_KEY)] ?: true

    suspend fun setShowLoginPref(value: Boolean) = context.dataStore.edit {
        it[booleanPreferencesKey(Keys.SHOW_LOGIN_KEY)] = value
    }

    suspend fun showWelcomeFlowPref() =
        context.dataStore.data.first()[booleanPreferencesKey(Keys.SHOW_WELCOME_KEY)] ?: true

    suspend fun setShowWelcomeFlowPref(value: Boolean) = context.dataStore.edit {
        it[booleanPreferencesKey(Keys.SHOW_WELCOME_KEY)] = value
    }

    fun showTutorial() = context.dataStore.data.map {
        it[booleanPreferencesKey(Keys.SHOW_MANAGE_TUTORIAL_KEY)] ?: true
    }

    suspend fun setShowTutorial(value: Boolean) = context.dataStore.edit {
        it[booleanPreferencesKey(Keys.SHOW_MANAGE_TUTORIAL_KEY)] = value
    }

    fun expNeededToShowCheckpoint() = context.dataStore.data.map {
        it[intPreferencesKey(Keys.SHOW_APP_CHECKPOINT_KEY)] ?: EXPERIENCE_TO_ENABLE_CHECKPOINT
    }

    suspend fun setExpToRetryCheckpoint() = context.dataStore.edit {
        val currentExp = it[intPreferencesKey(Keys.SHOW_APP_CHECKPOINT_KEY)] ?: EXPERIENCE_TO_ENABLE_CHECKPOINT
        val expToRetry = currentExp + EXPERIENCE_TO_RETRY_CHECKPOINT
        it[intPreferencesKey(Keys.SHOW_APP_CHECKPOINT_KEY)] = expToRetry
    }

    suspend fun disableCheckpoint() = context.dataStore.edit {
        it[intPreferencesKey(Keys.SHOW_APP_CHECKPOINT_KEY)] = EXPERIENCE_TO_DISABLE_CHECKPOINT
    }

    fun isExpSoundEnabled() = context.dataStore.data.map {
        it[booleanPreferencesKey(Keys.ENABLE_EXP_SOUND_KEY)] ?: true
    }

    suspend fun setExpSoundPref(value: Boolean) = context.dataStore.edit {
        it[booleanPreferencesKey(Keys.ENABLE_EXP_SOUND_KEY)] = value
    }

    fun isExerciseSoundEnabled() = context.dataStore.data.map {
        it[booleanPreferencesKey(Keys.ENABLE_EXERCISE_SOUND_KEY)] ?: true
    }

    suspend fun setExerciseSoundPref(value: Boolean) = context.dataStore.edit {
        it[booleanPreferencesKey(Keys.ENABLE_EXERCISE_SOUND_KEY)] = value
    }
}
