package com.puntogris.posture

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.preference.PreferenceDialogFragmentCompat
import com.puntogris.posture.utils.*

@Suppress("DEPRECATION")
class TimePreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    lateinit var timepicker: TimePicker

    override fun onCreateDialogView(context: Context?): View {
        timepicker = TimePicker(context)
        return timepicker
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        val minutesAfterMidnight = (preference as TimePickerPreference).getPersistedMinutesFromMidnight()
        val hours = minutesAfterMidnight.getHours()
        val minutes = minutesAfterMidnight.getMinutes()

        timepicker.apply {
            setIs24HourView(true)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                hour = hours
                minute = minutes
            }else {
                currentHour = hours
                currentMinute = minutes
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        // Save settings
        if(positiveResult) {
            val minutesAfterMidnight = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                 (timepicker.hour * 60) + timepicker.minute
            }else {
                (timepicker.currentHour * 60) + timepicker.currentMinute
            }
            (preference as TimePickerPreference).persistMinutesFromMidnight(minutesAfterMidnight)
            preference.summary = Utils.minutesFromMidnightToHourlyTime(minutesAfterMidnight)
        }
    }

    companion object {
        fun newInstance(key: String?): TimePreferenceDialogFragmentCompat {
            val fragment = TimePreferenceDialogFragmentCompat()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }

}