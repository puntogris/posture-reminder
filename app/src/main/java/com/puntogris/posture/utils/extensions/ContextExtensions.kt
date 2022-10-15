package com.puntogris.posture.utils.extensions

import android.content.Context
import android.content.res.Configuration
import android.os.PowerManager
import com.puntogris.posture.data.datasource.local.LocalDataSource

fun Context.isIgnoringBatteryOptimizations(): Boolean {
    return getSystemService(PowerManager::class.java).isIgnoringBatteryOptimizations(packageName)
}

fun Context.getVibrationPatternTitle(position: Int): String {
    return this.getString(LocalDataSource.vibrationPatterns[position].title)
}

fun Context.isDarkThemeOn() =
    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
