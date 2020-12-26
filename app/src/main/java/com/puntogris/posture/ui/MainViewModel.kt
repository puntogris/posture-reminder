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

    suspend fun saveStartTime(time:Int){
        reminderDao.updateStartTime(time)
    }

    suspend fun saveEndTime(time:Int){
        reminderDao.updateEndTime(time)
    }

    suspend fun saveTimeInterval(interval:Int){
        reminderDao.updateTimeInterval(interval)
    }

    suspend fun saveAlarmDays(){
        reminderDao.updateAlarmDays(arrayListOf(0,1,2,3,4,5))
    }

    fun isAppActive() = _reminder.value.isActive

    fun startAlarm(){
        viewModelScope.launch {
            alarm.startDailyAlarm(_reminder.value)
            reminderDao.updateReminderStatus(!_reminder.value.isActive)
        }
    }

    fun cancelAlarms(){
        viewModelScope.launch {
            alarm.cancelAlarms()
            reminderDao.updateReminderStatus(!_reminder.value.isActive)
        }
    }

    fun refreshAlarms(){
        alarm.cancelAlarms()
        alarm.startDailyAlarm(_reminder.value)
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