package com.puntogris.posture.feature_main.data.datasource.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.puntogris.posture.feature_auth.domain.model.UserPrivateData

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userPrivateData: UserPrivateData)

    @Query("SELECT * FROM UserPrivateData WHERE roomId = 1")
    suspend fun getUser(): UserPrivateData?

    @Query("SELECT uid FROM UserPrivateData WHERE roomId = 1")
    suspend fun getUserId(): String

    @Query("SELECT * FROM UserPrivateData WHERE roomId = 1")
    fun getUserLiveData(): LiveData<UserPrivateData>

    @Query("UPDATE UserPrivateData SET uid = :uid, username = :username, email = :email, photoUrl = :photoUrl WHERE roomId = 1")
    suspend fun updateCurrentUserData(
        uid: String,
        username: String,
        email: String,
        photoUrl: String
    )

    @Query("UPDATE UserPrivateData SET currentReminderId = :reminderId WHERE roomId = 1")
    suspend fun updateCurrentUserReminder(reminderId: String)

    @Query("UPDATE UserPrivateData SET username = :name WHERE roomId = 1")
    suspend fun updateUsername(name: String)

    @Query("UPDATE UserPrivateData SET experience = experience + :experience WHERE roomId = 1")
    suspend fun updateUserExperience(experience: Int)
}