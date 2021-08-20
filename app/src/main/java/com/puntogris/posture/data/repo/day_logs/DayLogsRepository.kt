package com.puntogris.posture.data.repo.day_logs

import com.puntogris.posture.data.local.DayLogsDao
import com.puntogris.posture.model.DayLog
import javax.inject.Inject

class DayLogsRepository @Inject constructor(
    private val dayLogsDao: DayLogsDao
): IDayLogsRepository {

    override fun getLastTwoDaysLogsLiveData() = dayLogsDao.getLastTwoEntries()

    override suspend fun getWeekDayLogs() = dayLogsDao.getWeekEntries()

}