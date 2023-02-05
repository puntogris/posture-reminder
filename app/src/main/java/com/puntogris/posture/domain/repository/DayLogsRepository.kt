package com.puntogris.posture.domain.repository

import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.model.RewardExp
import kotlinx.coroutines.flow.Flow

interface DayLogsRepository {

    fun getLastTwoDaysLogsStream(): Flow<List<DayLog>>

    fun getWeekDayLogsStream(): Flow<List<DayLog>>

    suspend fun updateDayLogAndUser(dayLog: DayLog): RewardExp
}