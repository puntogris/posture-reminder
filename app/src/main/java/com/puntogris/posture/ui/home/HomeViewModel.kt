package com.puntogris.posture.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.alarm.AlarmStatus
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dataStore: DataStore,
    dayLogsRepository: DayLogsRepository,
    private val alarm: Alarm,
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    val isAlarmActive = dataStore.isAlarmActiveFlow().asLiveData()

    val activeReminder = reminderRepository.getActiveReminderLiveData()

    private val _alarmStatus = MutableSharedFlow<AlarmStatus>()
    val alarmStatus: SharedFlow<AlarmStatus> = _alarmStatus

    fun toggleAlarm() {
        viewModelScope.launch {
            if (isAlarmActive.value!!) cancelAlarms() else startAlarm()
        }
    }

    private suspend fun startAlarm() {
        val activeReminder = reminderRepository.getActiveReminder()
        if (activeReminder == null) {
            _alarmStatus.emit(AlarmStatus.NotConfigured)
        } else {
            alarm.startDailyAlarm(activeReminder)
            _alarmStatus.emit(AlarmStatus.Activated(activeReminder))
        }
    }

    private suspend fun cancelAlarms() {
        alarm.cancelAlarms()
        _alarmStatus.emit(AlarmStatus.Canceled)
    }

    val isPandaAnimationEnabled = dataStore.showPandaAnimation().asLiveData()

    val getLastTwoDaysHistory = dayLogsRepository.getLastTwoDaysLogsLiveData()

    @RequiresApi(Build.VERSION_CODES.S)
    fun canScheduleExactAlarms() = alarm.canScheduleExactAlarms()

}