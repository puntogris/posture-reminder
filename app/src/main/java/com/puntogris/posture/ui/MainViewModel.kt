package com.puntogris.posture.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.puntogris.posture.Alarm
import com.puntogris.posture.data.local.ReminderDao
import com.puntogris.posture.data.remote.Repository
import com.puntogris.posture.model.ReminderConfig
import com.puntogris.posture.model.Report
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
        private val reminderDao: ReminderDao,
        private val repository: Repository,
        private val alarm: Alarm
    ): ViewModel() {

    private val _reminder = reminderDao.getReminderConfigLiveData()
    val reminder: LiveData<ReminderConfig> = _reminder

    suspend fun saveStartTime(time: Int){
        reminderDao.updateStartTime(time)
    }

    suspend fun saveEndTime(time: Int){
        reminderDao.updateEndTime(time)
    }

    suspend fun saveTimeInterval(interval: Int){
        reminderDao.updateTimeInterval(interval)
    }

    suspend fun saveAlarmDays(alarmDays: List<Int>){
        reminderDao.updateAlarmDays(alarmDays)
    }

    fun isAppActive() = reminder.value!!.isActive

    fun startAlarm(){
        viewModelScope.launch {
            reminder.value?.let {
                alarm.startDailyAlarm(it)
                reminderDao.updateReminderStatus(it.isActive)
            }
        }
    }

    fun cancelAlarms(){
        viewModelScope.launch {
            alarm.cancelAlarms()
            reminderDao.updateReminderStatus(!reminder.value!!.isActive)
        }
    }

    fun refreshAlarms(){
        alarm.cancelAlarms()
        alarm.startDailyAlarm(reminder.value!!)
    }

    fun enablePandaAnimation(){
        viewModelScope.launch {
            reminderDao.enablePandaAnimation()
        }
    }

    suspend fun sendReport(report:Report) = repository.sendReportToFirestore(report)
}