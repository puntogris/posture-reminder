package com.puntogris.posture.data.repo.user

import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.data.local.UserDao
import com.puntogris.posture.data.remote.FirebaseUserDataSource
import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.utils.Constants.USER_NAME_FIELD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firebaseUser: FirebaseUserDataSource,
    private val alarm: Alarm
): IUserRepository {

    override fun getUserFlowRoom() = userDao.getUserFlow()

    override fun getUserLiveDataRoom() = userDao.getUserLiveData()

    override suspend fun getUserRoom() = userDao.getUser()

    override suspend fun updateUsernameInRoomAndFirestore(name: String): Boolean = withContext(Dispatchers.IO){
        try {
            firebaseUser.runBatch().apply {
                update(firebaseUser.getUserPrivateDataRef(), USER_NAME_FIELD, name)
                update(firebaseUser.getUserPublicProfileRef(), USER_NAME_FIELD, name)
            }.commit().await()
            userDao.updateUsername(name)
            true
        }catch (e:Exception){
            false
        }
    }

    override suspend fun updateActiveReminderUserRoom(reminderId: String) {
        alarm.cancelAlarms()
        userDao.updateCurrentUserReminder(reminderId)
    }

    override fun isUserLoggedIn() = firebaseUser.getCurrentUser() != null

}