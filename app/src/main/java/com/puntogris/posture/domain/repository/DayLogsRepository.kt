package com.puntogris.posture.domain.repository

import androidx.lifecycle.LiveData
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.model.RewardExp

interface DayLogsRepository {

    fun getLastTwoDaysLogsLiveData(): LiveData<List<DayLog>>

    fun getWeekDayLogs(): LiveData<List<DayLog>>

    suspend fun updateDayLogAndUser(dayLog: DayLog): RewardExp
}