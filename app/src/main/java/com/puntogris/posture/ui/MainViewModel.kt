package com.puntogris.posture.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.puntogris.posture.Alarm
import com.puntogris.posture.data.ReminderDao
import com.puntogris.posture.data.Repository
import com.puntogris.posture.model.ReminderConfig
import com.puntogris.posture.utils.Utils.fromArrayList
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
        val days = fromArrayList(alarmDays)
        reminderDao.updateAlarmDays(days)
    }

    fun isAppActive() = reminder.value!!.isActive

    fun startAlarm(){
        viewModelScope.launch {
            alarm.startDailyAlarm(reminder.value!!)
            reminderDao.updateReminderStatus(!reminder.value!!.isActive)
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

    fun sendReport(message: String) {
        repository.sendReportToFirestore(message)
    }

}