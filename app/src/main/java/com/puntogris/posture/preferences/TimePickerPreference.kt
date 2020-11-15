package com.puntogris.posture.preferences

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.DialogPreference
import androidx.preference.PreferenceViewHolder
import com.puntogris.posture.R
import com.puntogris.posture.utils.Constants.DEFAULT_START_TIME_PERIOD_NOTIFICATIONS_PREF_VALUE
import com.puntogris.posture.utils.Utils

class TimePickerPreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val title = holder?.itemView?.findViewById<TextView>(android.R.id.title)
        val summary = holder?.itemView?.findViewById<TextView>(android.R.id.summary)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            title?.setTextColor(context.getColor(R.color.white))
            summary?.setTextColor(context.getColor(R.color.grey))

        }else{
            title?.setTextColor(context.resources.getColor(R.color.white))
            summary?.setTextColor(context.resources.getColor(R.color.grey))
        }
    }

    private val mDialogLayoutResId: Int = R.layout.pref_dialog_time

    override fun getDialogLayoutResource(): Int {
        return mDialogLayoutResId
    }

    // Get saved preference value (in minutes from midnight, so 1 AM is represented as 1*60 here
    fun getPersistedMinutesFromMidnight(): Int {
        return super.getPersistedInt(DEFAULT_START_TIME_PERIOD_NOTIFICATIONS_PREF_VALUE)
    }

    // Save preference
    fun persistMinutesFromMidnight(minutesFromMidnight: Int) {
        super.persistInt(minutesFromMidnight)
        notifyChanged()
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        super.onSetInitialValue(defaultValue)
        summary = Utils.minutesFromMidnightToHourlyTime(getPersistedMinutesFromMidnight())
    }


}