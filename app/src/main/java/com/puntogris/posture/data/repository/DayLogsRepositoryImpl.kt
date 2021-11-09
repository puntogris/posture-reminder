package com.puntogris.posture.data.repository

import androidx.room.withTransaction
import com.puntogris.posture.data.datasource.local.db.AppDatabase
import com.puntogris.posture.data.datasource.local.db.DayLogsDao
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.RewardExp
import kotlinx.coroutines.withContext

class DayLogsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dispatchers: DispatcherProvider
) : DayLogsRepository {

    override fun getLastTwoDaysLogsLiveData() = appDatabase.dayLogsDao.getLastTwoEntries()

    override suspend fun getWeekDayLogs() = appDatabase.dayLogsDao.getWeekEntries()

    override suspend fun updateLocalDayLogAndUser(dayLog: DayLog) = withContext(dispatchers.io) {
        try {
            val todayLog = appDatabase.dayLogsDao.getTodayLog()
            when {
                todayLog == null -> {
                    insertNewDayLog(dayLog)
                    RewardExp.Success
                }
                todayLog.expGained + dayLog.expGained <= 100 -> {
                    todayLog.apply {
                        exercises += dayLog.exercises
                        expGained += dayLog.expGained
                        notifications += dayLog.notifications
                    }
                    appDatabase.apply {
                        withTransaction {
                            dayLogsDao.update(todayLog)
                            userDao.updateUserExperience(dayLog.expGained)
                        }
                    }
                    RewardExp.Success
                }
                else -> RewardExp.ExpLimit
            }
        } catch (e: Exception) {
            RewardExp.Error
        }
    }

    private suspend fun insertNewDayLog(dayLog: DayLog) {
        withContext(dispatchers.io) {
            appDatabase.apply {
                withTransaction {
                    userDao.updateUserExperience(dayLog.expGained)
                    dayLogsDao.insert(dayLog)
                }
            }
        }
    }

}