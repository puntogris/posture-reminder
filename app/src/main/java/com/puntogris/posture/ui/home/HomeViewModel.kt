package com.puntogris.posture.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.alarm.AlarmStatus
import com.puntogris.posture.data.repo.day_logs.DayLogsRepository
import com.puntogris.posture.data.repo.reminder.ReminderRepository
import com.puntogris.posture.data.datasource.local.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val alarm: Alarm,
    private val dataStore: DataStore,
    private val dayLogsRepository: DayLogsRepository,
    private val reminderRepository: ReminderRepository
):ViewModel() {

    val isAlarmActive = dataStore.isAlarmActive().asLiveData()

    val activeReminder = reminderRepository.getActiveReminderLiveData()

    private val _alarmStatus = MutableSharedFlow<AlarmStatus>()
    val alarmStatus: SharedFlow<AlarmStatus> = _alarmStatus

    fun toggleAlarm(){
        viewModelScope.launch {
            if (isAlarmActive.value!!) cancelAlarms() else startAlarm()
        }
    }

    private suspend fun startAlarm(){
        val activeReminder = reminderRepository.getActiveReminder()
        if (activeReminder == null){
            _alarmStatus.emit(AlarmStatus.NotConfigured)
        }else{
            alarm.startDailyAlarm(activeReminder)
            _alarmStatus.emit(AlarmStatus.Activated(activeReminder))
        }
    }

    private suspend fun cancelAlarms(){
        alarm.cancelAlarms()
        _alarmStatus.emit(AlarmStatus.Canceled)
    }

    val isPandaAnimationEnabled = liveData {
        emit(dataStore.showPandaAnimation())
    }

    fun getLastTwoDaysHistory() = dayLogsRepository.getLastTwoDaysLogsLiveData()

    @RequiresApi(Build.VERSION_CODES.S)
    fun canScheduleExactAlarms() = alarm.canScheduleExactAlarms()


}