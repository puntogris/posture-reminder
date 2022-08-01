package com.puntogris.posture.utils

import android.content.Context
import androidx.annotation.StringRes
import com.puntogris.posture.R
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.Utils.minutesFromMidnightToHourlyTime
import com.puntogris.posture.utils.extensions.getVibrationPatternTitle

sealed class ReminderUi {

    abstract fun update(reminder: Reminder, context: Context): Boolean

    sealed class Item(@StringRes val title: Int, var description: String) : ReminderUi() {

        class Interval(
            title: Int = R.string.interval_required,
            description: String
        ) : Item(title, description) {

            private var lastValue = 0

            override fun update(reminder: Reminder, context: Context): Boolean {
                return (reminder.timeInterval != 0 && lastValue != reminder.timeInterval).also {
                    if (it) {
                        description = reminder.timeIntervalSummary()
                        lastValue = reminder.timeInterval
                    }
                }
            }
        }

        class Start(
            title: Int = R.string.start_time_required,
            description: String
        ) : Item(title, description) {

            private var lastValue = -1

            override fun update(reminder: Reminder, context: Context): Boolean {
                return (reminder.startTime != -1 && lastValue != reminder.startTime).also {
                    if (it) {
                        description = minutesFromMidnightToHourlyTime(reminder.startTime)
                        lastValue = reminder.startTime
                    }
                }
            }
        }

        class End(
            title: Int = R.string.end_time_required,
            description: String
        ) : Item(title, description) {

            private var lastValue = -1

            override fun update(reminder: Reminder, context: Context): Boolean {
                return (reminder.endTime != -1 && lastValue != reminder.endTime).also {
                    if (it) {
                        description = minutesFromMidnightToHourlyTime(reminder.endTime)
                        lastValue = reminder.endTime
                    }
                }
            }
        }

        class Days(
            title: Int = R.string.days_required,
            description: String
        ) : Item(title, description) {

            private var lastValue = emptyList<Int>()

            override fun update(reminder: Reminder, context: Context): Boolean {
                return (reminder.alarmDays.isNotEmpty() && lastValue != reminder.alarmDays).also {
                    if (it) {
                        val days = context.resources.getStringArray(R.array.alarmDays)
                        description = reminder.alarmDaysSummary(days)
                        lastValue = reminder.alarmDays
                    }
                }
            }
        }

        class Sound(title: Int = R.string.sound, description: String) : Item(title, description) {

            private var lastValue = ""

            override fun update(reminder: Reminder, context: Context): Boolean {
                return (lastValue != reminder.soundName).also {
                    if (it) {
                        description = reminder.soundName
                        lastValue = reminder.soundName
                    }
                }
            }
        }

        class Vibration(
            title: Int = R.string.vibration,
            description: String
        ) : Item(title, description) {

            private var lastValue = 0

            override fun update(reminder: Reminder, context: Context): Boolean {
                return (lastValue != reminder.vibrationPattern).also {
                    if (it) {
                        description = context.getVibrationPatternTitle(reminder.vibrationPattern)
                        lastValue = reminder.vibrationPattern
                    }
                }
            }
        }
        class Name(
            title: Int = R.string.name_required,
            description: String
        ) : Item(title, description) {

            private var lastValue: String = ""

            override fun update(reminder: Reminder, context: Context): Boolean {
                return (lastValue != reminder.name).also {
                    if (it) {
                        description = reminder.name
                        lastValue = reminder.name
                    }
                }
            }
        }
    }

    class Color(var color: Int = R.color.grey) : ReminderUi() {
        override fun update(reminder: Reminder, context: Context): Boolean {
            return (reminder.color != color).also {
                if (it) color = reminder.color
            }
        }
    }
}
