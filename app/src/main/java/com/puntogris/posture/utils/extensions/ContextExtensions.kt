package com.puntogris.posture.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.PowerManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.puntogris.posture.data.datasource.local.LocalDataSource


fun Context.isIgnoringBatteryOptimizations(): Boolean {
    val pm = getSystemService(PowerManager::class.java)
    return (pm.isIgnoringBatteryOptimizations(packageName))
}

fun Context.getVibrationPatternTitle(position: Int): String {
    return this.getString(LocalDataSource.vibrationPatterns[position].title)
}

fun Context.isDarkThemeOn() =
    (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}