package com.puntogris.posture

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.puntogris.posture.utils.Utils

class TimePickerPreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {

    private val mDialogLayoutResId: Int = R.layout.pref_dialog_time

    override fun getDialogLayoutResource(): Int {
        return mDialogLayoutResId
    }

    // Get saved preference value (in minutes from midnight, so 1 AM is represented as 1*60 here
    fun getPersistedMinutesFromMidnight(): Int {
        return super.getPersistedInt(DEFAULT_MINUTES_FROM_MIDNIGHT)
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

    // Mostly for default values
    companion object {
        // By default we want notification to appear at 9 AM each time.
        private const val DEFAULT_HOUR = 9
        const val DEFAULT_MINUTES_FROM_MIDNIGHT = DEFAULT_HOUR * 60
    }

}