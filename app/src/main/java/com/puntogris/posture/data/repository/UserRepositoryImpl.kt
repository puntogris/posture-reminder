package com.puntogris.posture.data.repository

import com.puntogris.posture.data.datasource.local.db.AppDatabase
import com.puntogris.posture.data.datasource.remote.FirebaseClients
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.model.Ticket
import com.puntogris.posture.domain.repository.UserRepository
import com.puntogris.posture.domain.repository.UserServerApi
import com.puntogris.posture.framework.alarm.Alarm
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val firebaseClients: FirebaseClients,
    private val userServerApi: UserServerApi,
    private val alarm: Alarm,
    private val dispatchers: DispatcherProvider,
    private val appDatabase: AppDatabase
) : UserRepository {

    override fun isUserLoggedIn() = firebaseClients.getCurrentUser != null

    override fun getUserStream() = appDatabase.userDao.getUserStream()

    override suspend fun getUser() = appDatabase.userDao.getUser()

    override suspend fun updateUsername(name: String) = withContext(dispatchers.io) {
        SimpleResult.build {
            userServerApi.updateUsername(name)
            appDatabase.userDao.updateUsername(name)
        }
    }

    override suspend fun updateActiveReminder(reminder: Reminder) {
        appDatabase.userDao.updateCurrentUserReminder(reminder.reminderId)
        alarm.cancelAlarms()
    }

    override suspend fun deleteUserAccount(): SimpleResult = withContext(dispatchers.io) {
        SimpleResult.build {
            userServerApi.deleteAccount()
            appDatabase.clearAllTables()
        }
    }

    override fun sendTicket(message: String): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading())
            val ticket = Ticket(
                uid = firebaseClients.currentUid.orEmpty(),
                message = message
            )
            userServerApi.sendTicket(ticket)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error())
        }
    }.flowOn(dispatchers.io)
}
