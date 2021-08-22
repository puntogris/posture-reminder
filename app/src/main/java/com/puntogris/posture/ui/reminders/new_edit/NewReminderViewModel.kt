package com.puntogris.posture.ui.reminders.new_edit

import androidx.lifecycle.*
import com.puntogris.posture.data.repo.reminder.ReminderRepository
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.model.ToneItem
import com.puntogris.posture.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class NewReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _reminder = MutableLiveData(Reminder())
    val reminder: LiveData<Reminder> = _reminder

    suspend fun saveReminder() = reminderRepository.insertReminder(_reminder.value!!)

    fun updateReminder(reminder: Reminder) {
        _reminder.value = reminder
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

    fun saveReminderVibrationPattern(position: Int){
        _reminder.setField { vibrationPattern = position }
    }

    fun saveReminderSoundPattern(toneItem: ToneItem?){
        toneItem?.let {
            _reminder.setField {
                soundUri = it.uri
                soundName = it.title
            }
        }
    }

    fun isReminderValid() = reminder.value!!.isValid()
}