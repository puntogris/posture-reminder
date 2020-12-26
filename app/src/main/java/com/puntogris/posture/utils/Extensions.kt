package com.puntogris.posture.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.maxkeppeler.bottomsheets.options.Option

import com.puntogris.posture.R
import com.puntogris.posture.model.ReminderConfig
import java.util.*
import kotlin.collections.ArrayList

fun Int.getHours() = this / 60

fun Int.getMinutes() = this % 60

fun Long.millisToMinutes() = (this / 1000 / 60).toInt()

fun Int.minutesToMillis() = this * 1000 * 60

fun Calendar.setMidnight(){
    this.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun Fragment.createNotificationChannel(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "notification posture"
        val descriptionText = "channel for posture notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Constants.POSTURE_NOTIFICATION_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun Fragment.createSnackBar(message: String){
    val snackLayout = this.requireActivity().findViewById<View>(android.R.id.content)
    Snackbar.make(snackLayout, message, Snackbar.LENGTH_LONG).show()
}

fun AppCompatActivity.getNavController() =
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

@BindingAdapter("reminderSummaryStatus")
fun TextView.setReminderSummaryStatus(reminder: ReminderConfig?){
    if(reminder != null) text = if (reminder.isActive) context.getString(R.string.alarm_on) else context.getString(R.string.alarm_off)
}

@BindingAdapter("reminderStatus")
fun TextView.setReminderStatus(reminder: ReminderConfig?){
    if(reminder != null) text = if (reminder.isActive) context.getString(R.string.stop_alarm) else context.getString(R.string.start_alarm)
}

fun PreferenceFragmentCompat.preference(key:String)=
    findPreference<Preference>(key)
