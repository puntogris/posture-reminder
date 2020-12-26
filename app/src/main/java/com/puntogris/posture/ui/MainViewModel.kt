package com.puntogris.posture.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.room.TypeConverters
import com.puntogris.posture.Alarm
import com.puntogris.posture.data.Converters
import com.puntogris.posture.data.ReminderDao
import com.puntogris.posture.data.Repository
import com.puntogris.posture.model.ReminderConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val reminderDao: ReminderDao,
    private val repository: Repository,
    private val alarm: Alarm
    ): ViewModel() {

    private val _reminder = MutableStateFlow(ReminderConfig())
    val reminder = _reminder.asLiveData()

    init {
        viewModelScope.launch {
            reminderDao.getReminderConfigFlow().collect {
                if (it != null) _reminder.emit(it)
                else initDatabase()
            }
        }
    }

    private fun initDatabase(){
        viewModelScope.launch {
            reminderDao.insert(ReminderConfig())
        }
    }

    fun saveStartTime(time:Int){
        viewModelScope.launch {
            reminderDao.updateStartTime(time)
        }
    }

    fun saveEndTime(time:Int){
        viewModelScope.launch {
            reminderDao.updateEndTime(time)
        }
    }

    fun saveTimeInterval(interval:Int){
        viewModelScope.launch {
            reminderDao.updateTimeInterval(interval)
        }
    }

    fun saveAlarmDays(){
        viewModelScope.launch {
            reminderDao.updateAlarmDays(arrayListOf(0,1,2,3,4,5))
        }
    }

    fun changeAppStatus(){
        viewModelScope.launch {
            val currentStatus = _reminder.value.isActive
            if (currentStatus) alarm.cancelAlarms()
            else alarm.startDailyAlarm(_reminder.value)
            reminderDao.updateReminderStatus(!currentStatus)
        }
    }

    fun isAppActive() = _reminder.value.isActive

    fun startAlarm(){
        viewModelScope.launch {
            reminderDao.updateReminderStatus(!_reminder.value.isActive)
        }
    }

    fun cancelAlarms(){
        alarm.cancelAlarms()
        viewModelScope.launch {
            reminderDao.updateReminderStatus(!_reminder.value.isActive)
        }
    }


    fun enablePandaAnimation(){
        viewModelScope.launch {
            reminderDao.enablePandaAnimation()
        }
    }

    fun sendReport(message: String){
        repository.sendReportToFirestore(message)
    }

}