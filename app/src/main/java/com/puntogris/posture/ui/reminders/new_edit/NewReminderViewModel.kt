package com.puntogris.posture.ui.reminders.new_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.puntogris.posture.R
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.ReminderId
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var initialReminderCopy: Reminder? = null

    private val _reminder = MutableStateFlow(Reminder())
    val reminder = _reminder.asStateFlow()

    init {
        savedStateHandle.get<Reminder>("reminder")?.let {
            _reminder.value = it
            initialReminderCopy = it.copy()
        }
    }

    suspend fun saveReminder(): Result<ReminderId> {
        return when {
            !reminder.value.requiredInfoValid() -> {
                Result.Error(R.string.snack_reminder_not_valid)
            }
            !reminderWasEdited() -> {
                Result.Success(ReminderId(reminder.value.reminderId))
            }
            else -> {
                reminderRepository.insertReminder(_reminder.value)
            }
        }
    }

    private fun reminderWasEdited() = initialReminderCopy != _reminder.value

    fun saveReminderName(text: String) {
        _reminder.value.name = text
    }

    fun saveStartTime(time: Long) {
        _reminder.value = _reminder.value.copy(startTime = time.millisToMinutes())
    }

    fun saveEndTime(time: Long) {
        _reminder.value = _reminder.value.copy(endTime = time.millisToMinutes())
    }

    fun saveTimeInterval(time: Int) {
        _reminder.value = _reminder.value.copy(timeInterval = time)
    }

    fun saveReminderDays(days: List<Int>) {
        _reminder.value = _reminder.value.copy(alarmDays = days)

    }

    fun saveReminderColor(resource: Int) {
        _reminder.value = _reminder.value.copy(color = resource)
    }

    fun saveReminderVibrationPattern(position: Int) {
        println("af")
        _reminder.value = _reminder.value.copy(vibrationPattern = position)
    }

    fun saveReminderSoundPattern(toneItem: ToneItem?) {
        println("a")
        toneItem?.let {
            _reminder.value = _reminder.value.copy(
                soundUri = it.uri,
                soundName = it.title
            )
        }
    }

    fun getDefaultClockTimeInMillis(code: ReminderUi.Item): Long {
        val isNewReminder = reminder.value.reminderId.isBlank()

        val date = if (isNewReminder) Date()
        else Utils.getDateFromMinutesSinceMidnight(getReminderTime(code))

        return date.timeWithZoneOffset
    }

    private fun getReminderTime(code: ReminderUi.Item): Int {
        return if (code is ReminderUi.Item.Start) reminder.value.startTime
        else reminder.value.endTime
    }

}