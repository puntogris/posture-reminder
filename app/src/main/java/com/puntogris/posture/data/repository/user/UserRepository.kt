package com.puntogris.posture.data.repository.user

import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.data.DispatcherProvider
import com.puntogris.posture.data.datasource.local.room.dao.UserDao
import com.puntogris.posture.data.datasource.remote.FirebaseUserDataSource
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.utils.Constants.USER_NAME_FIELD
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firebaseUser: FirebaseUserDataSource,
    private val alarm: Alarm,
    private val dispatchers: DispatcherProvider
): IUserRepository {

    override fun isUserLoggedIn() = firebaseUser.getCurrentUser() != null

    override fun getLocalUserLiveData() = userDao.getUserLiveData()

    override suspend fun getLocalUser() = userDao.getUser()

    override suspend fun updateLocalAndServerUsername(name: String) = withContext(dispatchers.io){
        SimpleResult.build {
            firebaseUser.runBatch().apply {
                update(firebaseUser.getUserPrivateDataRef(), USER_NAME_FIELD, name)
                update(firebaseUser.getUserPublicProfileRef(), USER_NAME_FIELD, name)
            }.commit().await()
            userDao.updateUsername(name)
        }
    }

    override suspend fun updateLocalActiveReminder(reminder: Reminder) {
        userDao.updateCurrentUserReminder(reminder.reminderId)
        alarm.refreshAlarms(reminder)
    }
}