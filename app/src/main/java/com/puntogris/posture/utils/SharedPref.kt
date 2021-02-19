package com.puntogris.posture.utils

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext private val context: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun hideAnnouncementPref(){
        sharedPref.edit().putBoolean("announcement_pref", false).apply()
    }

    fun showAnnouncementPref() =
        sharedPref.getBoolean("announcement_pref", true)

}