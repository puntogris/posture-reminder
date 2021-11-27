package com.puntogris.posture.feature_main.domain.repository

import androidx.lifecycle.LiveData
import com.puntogris.posture.feature_main.domain.model.DayLog
import com.puntogris.posture.feature_main.domain.model.RewardExp

interface DayLogsRepository {

    fun getLastTwoDaysLogsLiveData(): LiveData<List<DayLog>>

    suspend fun getWeekDayLogs(): List<DayLog>

    suspend fun updateDayLogAndUser(dayLog: DayLog): RewardExp
}