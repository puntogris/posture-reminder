package com.puntogris.posture.ui.reminders.new_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.ReminderId
import com.puntogris.posture.model.ToneItem
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.millisToMinutes
import com.puntogris.posture.utils.setField
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private var initialReminderCopy: Reminder? = null

    private val _reminder = MutableLiveData(Reminder())
    val reminder: LiveData<Reminder> = _reminder

    suspend fun saveReminder(): Result<ReminderId> {
        return if (reminderIsEdited()) reminderRepository.insertReminder(_reminder.value!!)
        else Result.Success(ReminderId(reminder.value!!.reminderId))
    }

    fun updateReminder(reminder: Reminder) {
        _reminder.value = reminder
        initialReminderCopy = reminder.copy()
    }

    private fun reminderIsEdited(): Boolean {
        return initialReminderCopy != _reminder.value
    }

    fun saveReminderName(text: String) {
        _reminder.value?.name = text
    }

    fun saveStartTime(time: Long) {
        _reminder.setField { startTime = time.millisToMinutes() }
    }

    fun saveEndTime(time: Long) {
        _reminder.setField { endTime = time.millisToMinutes() }
    }

    fun saveTimeInterval(time: Int) {
        _reminder.setField { timeInterval = time }
    }

    fun saveReminderDays(days: List<Int>) {
        _reminder.setField { alarmDays = days }
    }

    fun saveReminderColor(resource: Int) {
        _reminder.setField { color = resource }
    }

    fun saveReminderVibrationPattern(position: Int) {
        _reminder.setField { vibrationPattern = position }
    }

    fun saveReminderSoundPattern(toneItem: ToneItem?) {
        toneItem?.let {
            _reminder.setField {
                soundUri = it.uri
                soundName = it.title
            }
        }
    }

    fun isReminderValid() = reminder.value!!.requiredInfoValid()

}