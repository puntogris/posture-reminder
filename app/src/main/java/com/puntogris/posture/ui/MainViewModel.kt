package com.puntogris.posture.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.puntogris.posture.Alarm
import com.puntogris.posture.data.ReminderDao
import com.puntogris.posture.data.Repository
import com.puntogris.posture.model.ReminderConfig
import com.puntogris.posture.utils.Utils.fromArrayList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val reminderDao: ReminderDao,
    private val repository: Repository,
    private val alarm: Alarm
    ): ViewModel() {

    private val _reminder = MutableStateFlow(ReminderConfig())
    val reminder:LiveData<ReminderConfig> = _reminder.asLiveData()

    init {
        viewModelScope.launch {
            reminderDao.getReminderConfigFlow().collect {
                _reminder.emit(it)
            }
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

    suspend fun saveAlarmDays(alarmDays: List<Int>){
        val days = fromArrayList(alarmDays)
        reminderDao.updateAlarmDays(days)
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

    fun sendReport(message: String) {
        repository.sendReportToFirestore(message)
    }

}