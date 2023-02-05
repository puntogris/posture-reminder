package com.puntogris.posture.domain.repository

import androidx.lifecycle.LiveData
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.model.RewardExp
import kotlinx.coroutines.flow.Flow

interface DayLogsRepository {

    fun getLastTwoDaysLogsStream(): Flow<List<DayLog>>

    fun getWeekDayLogsStream(): LiveData<List<DayLog>>

    suspend fun updateDayLogAndUser(dayLog: DayLog): RewardExp
}