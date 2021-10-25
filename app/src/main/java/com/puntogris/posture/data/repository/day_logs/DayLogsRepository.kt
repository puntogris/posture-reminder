package com.puntogris.posture.data.repository.day_logs

import androidx.room.withTransaction
import com.puntogris.posture.data.DispatcherProvider
import com.puntogris.posture.data.datasource.local.room.db.AppDatabase
import com.puntogris.posture.data.datasource.local.room.dao.DayLogsDao
import com.puntogris.posture.data.datasource.local.room.dao.UserDao
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.utils.RewardExp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DayLogsRepository @Inject constructor(
    private val dayLogsDao: DayLogsDao,
    private val appDatabase: AppDatabase,
    private val userDao: UserDao,
    private val dispatchers: DispatcherProvider
): IDayLogsRepository {

    override fun getLastTwoDaysLogsLiveData() = dayLogsDao.getLastTwoEntries()

    override suspend fun getWeekDayLogs() = dayLogsDao.getWeekEntries()

    override suspend fun updateRoomDayLogAndUser(dayLog: DayLog) = withContext(dispatchers.io){
        try {
            val todayLog = dayLogsDao.getTodayLog()
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
                    appDatabase.withTransaction {
                        dayLogsDao.update(todayLog)
                        userDao.updateUserExperience(dayLog.expGained)
                    }
                    RewardExp.Success
                }
                else -> RewardExp.ExpLimit
            }
        }catch (e:Exception){
            RewardExp.Error
        }
    }

    private suspend fun insertNewDayLog(dayLog: DayLog){
        appDatabase.withTransaction {
            userDao.updateUserExperience(dayLog.expGained)
            dayLogsDao.insert(dayLog)
        }
    }

}