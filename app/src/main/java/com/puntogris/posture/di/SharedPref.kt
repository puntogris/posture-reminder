package com.puntogris.posture.di

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ActivityContext private val context:Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    private val defaultAppStatus = true

    private fun getAppStatus() = sharedPref.getBoolean("app_status", defaultAppStatus)

    fun appStatusText() = if (getAppStatus()) "Enabled" else "Disabled"

    fun changeAppStatus(): String{
        sharedPref.edit().putBoolean("app_status", !getAppStatus()).apply()
        return appStatusText()
    }

}