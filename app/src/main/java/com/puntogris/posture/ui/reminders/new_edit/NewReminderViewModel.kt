package com.puntogris.posture.ui.reminders.new_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.puntogris.posture.R
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.ReminderId
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.utils.ReminderUi
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.ToneItem
import com.puntogris.posture.utils.Utils.getDateFromMinutesSinceMidnight
import com.puntogris.posture.utils.extensions.millisToMinutes
import com.puntogris.posture.utils.extensions.timeWithZoneOffset
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

private const val REMINDER_KEY = "reminder"

@HiltViewModel
class NewReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialReminderCopy = savedStateHandle.get<Reminder>(REMINDER_KEY)?.copy()

    val reminder = savedStateHandle.getStateFlow(REMINDER_KEY, Reminder())

    suspend fun saveReminder(): Result<ReminderId> {
        return when {
            !reminder.value.requiredInfoValid() -> Result.Error(R.string.snack_reminder_not_valid)
            !reminderWasEdited() -> Result.Success(ReminderId(reminder.value.reminderId))
            else -> reminderRepository.insertReminder(reminder.value)
        }
    }

    private fun reminderWasEdited() = initialReminderCopy != reminder.value

    fun saveReminderName(text: String) {
        savedStateHandle[REMINDER_KEY] = reminder.value.copy(name = text)
    }

    fun saveStartTime(time: Long) {
        savedStateHandle[REMINDER_KEY] = reminder.value.copy(startTime = time.millisToMinutes())
    }

    fun saveEndTime(time: Long) {
        savedStateHandle[REMINDER_KEY] = reminder.value.copy(endTime = time.millisToMinutes())
    }

    fun saveTimeInterval(time: Int) {
        savedStateHandle[REMINDER_KEY] = reminder.value.copy(timeInterval = time)
    }

    fun saveReminderDays(days: List<Int>) {
        savedStateHandle[REMINDER_KEY] = reminder.value.copy(alarmDays = days)
    }

    fun saveReminderColor(color: Int) {
        savedStateHandle[REMINDER_KEY] = reminder.value.copy(color = color)
    }

    fun saveReminderVibrationPattern(position: Int) {
        savedStateHandle[REMINDER_KEY] = reminder.value.copy(vibrationPattern = position)
    }

    fun saveReminderSoundPattern(toneItem: ToneItem?) {
        toneItem?.let {
            savedStateHandle[REMINDER_KEY] = reminder.value.copy(
                soundUri = it.uri,
                soundName = it.title
            )
        }
    }

    fun getDefaultClockTimeInMillis(code: ReminderUi.Item): Long {
        val date = when {
            code is ReminderUi.Item.Start && reminder.value.startTimeValid -> {
                getDateFromMinutesSinceMidnight(reminder.value.startTime)
            }
            code is ReminderUi.Item.End && reminder.value.endTimeValid -> {
                getDateFromMinutesSinceMidnight(reminder.value.endTime)
            }
            else -> Date()
        }
        return date.timeWithZoneOffset
    }
}
