package com.puntogris.posture.data.repo.user

import com.puntogris.posture.data.local.UserDao
import com.puntogris.posture.data.remote.FirebaseReminderDataSource
import com.puntogris.posture.data.remote.FirebaseUserDataSource
import com.puntogris.posture.model.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val firebaseUser: FirebaseUserDataSource
): IUserRepository {

    override fun getUserFlowRoom() = userDao.getUserFlow()

    override fun getUserLiveDataRoom() = userDao.getUserLiveData()

    override suspend fun updateUsernameInRoomAndFirestore(name: String): SimpleResult = withContext(Dispatchers.IO){
        try {
            firebaseUser.runBatch().apply {
                update(firebaseUser.getUserPrivateDataRef(), "name", name)
                update(firebaseUser.getUserPublicProfileRef(), "name", name)
            }.commit().await()
            userDao.updateUsername(name)
            SimpleResult.Success
        }catch (e:Exception){
            SimpleResult.Failure
        }
    }

    override suspend fun updateActiveReminderUserRoom(reminderId: String) {
        userDao.updateCurrentUserReminder(reminderId)
    }


}