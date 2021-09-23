package com.puntogris.posture.data.repo.sync

import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.utils.SimpleResult

interface ISyncRepository {
    suspend fun syncFirestoreAccountWithRoom(userPrivateData: UserPrivateData): SimpleResult
    suspend fun syncUserExperienceInFirestoreWithRoom()
}