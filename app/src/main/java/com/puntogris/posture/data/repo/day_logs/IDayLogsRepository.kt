package com.puntogris.posture.data.repo.day_logs

import androidx.lifecycle.LiveData
import com.puntogris.posture.model.DayLog

interface IDayLogsRepository {
    fun getLastTwoDaysLogsLiveData(): LiveData<List<DayLog>>
    suspend fun getWeekDayLogs(): List<DayLog>
}