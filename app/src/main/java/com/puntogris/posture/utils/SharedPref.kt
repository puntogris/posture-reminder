package com.puntogris.posture.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.puntogris.posture.utils.Constants.DEFAULT_SHOW_NOTIFICATIONS_PREF_VALUE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun hideAnnouncementPref(){
        sharedPref.edit().putBoolean("announcement_pref", false).apply()
    }

    fun showAnnouncementPref() = sharedPref.getBoolean("announcement_pref", true)

    fun getNotificationsPref(): Boolean{
        return sharedPref.getBoolean("pref_show_notifications", DEFAULT_SHOW_NOTIFICATIONS_PREF_VALUE)
    }


}