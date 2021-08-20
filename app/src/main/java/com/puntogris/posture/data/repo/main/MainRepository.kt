package com.puntogris.posture.data.repo.main

import com.puntogris.posture.data.remote.FirebaseDataSource
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): IMainRepository{

    override fun isUserLoggedIn() = firebaseDataSource.getCurrentUser() != null
}