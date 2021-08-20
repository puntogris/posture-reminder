package com.puntogris.posture.ui

import androidx.lifecycle.*
import com.puntogris.posture.Alarm
import com.puntogris.posture.data.repo.day_logs.DayLogsRepository
import com.puntogris.posture.data.repo.main.MainRepository
import com.puntogris.posture.utils.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val alarm: Alarm,
    private val dayLogsRepository: DayLogsRepository,
    dataStore: DataStore
    ): ViewModel() {

    val reminder = dataStore.currentReminderState().asLiveData()

    fun startAlarm(){
        viewModelScope.launch {
//            reminder.value?.let {
//                alarm.startDailyAlarm(it)
//            //    reminderDao.updateReminderStatus(it.isActive)
//            }
        }
    }

    fun cancelAlarms(){
        viewModelScope.launch {
            alarm.cancelAlarms()
          //  reminderDao.updateReminderStatus(!reminder.value!!.isActive)
        }
    }

    fun refreshAlarms(){
        alarm.cancelAlarms()
      //  alarm.startDailyAlarm(reminder.value!!)
    }


    fun getLastTwoDaysHistory() = dayLogsRepository.getLastTwoDaysLogsLiveData()

}