package com.puntogris.posture.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.framework.alarm.Alarm
import com.puntogris.posture.framework.alarm.AlarmStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dataStoreHelper: DataStoreHelper,
    dayLogsRepository: DayLogsRepository,
    private val alarm: Alarm,
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    val isAlarmActive = dataStoreHelper.isAlarmActiveFlow().asLiveData()

    val activeReminder = reminderRepository.getActiveReminderLiveData()

    private val _alarmStatus = MutableSharedFlow<AlarmStatus>()
    val alarmStatus = _alarmStatus.asSharedFlow()

    fun toggleAlarm() {
        viewModelScope.launch {
            if (isAlarmActive.value == true) {
                cancelAlarms()
            }else {
                startAlarm()
            }
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

    val isPandaAnimationEnabled = dataStoreHelper.showPandaAnimation().asLiveData()

    val getLastTwoDaysHistory = dayLogsRepository.getLastTwoDaysLogsLiveData()

    @RequiresApi(Build.VERSION_CODES.S)
    fun canScheduleExactAlarms() = alarm.canScheduleExactAlarms()
}