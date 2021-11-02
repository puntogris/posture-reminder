package com.puntogris.posture.data.repository.day_logs

import androidx.lifecycle.LiveData
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.utils.RewardExp

interface DayLogsRepository {
    fun getLastTwoDaysLogsLiveData(): LiveData<List<DayLog>>
    suspend fun getWeekDayLogs(): List<DayLog>
    suspend fun updateLocalDayLogAndUser(dayLog: DayLog): RewardExp
}