package com.puntogris.posture.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.repository.ReminderRepository
import com.puntogris.posture.framework.alarm.Alarm
import com.puntogris.posture.framework.alarm.AlarmStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dataStoreHelper: DataStoreHelper,
    dayLogsRepository: DayLogsRepository,
    private val alarm: Alarm,
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    val isAlarmActive = dataStoreHelper
        .isAlarmActiveFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val activeReminder = reminderRepository
        .getActiveReminderStream()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _alarmStatus = MutableSharedFlow<AlarmStatus>()
    val alarmStatus = _alarmStatus.asSharedFlow()

    fun toggleAlarm() {
        viewModelScope.launch {
            if (isAlarmActive.value) {
                cancelAlarms()
            } else {
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

    val isPandaAnimationEnabled = dataStoreHelper.showPandaAnimation()

    val getLastTwoDaysHistory = dayLogsRepository.getLastTwoDaysLogsStream()
}
