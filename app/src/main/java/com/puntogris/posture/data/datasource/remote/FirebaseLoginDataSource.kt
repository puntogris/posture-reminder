package com.puntogris.posture.data.datasource.remote

import javax.inject.Inject

class FirebaseLoginDataSource @Inject constructor(): FirebaseDataSource() {

    fun signOutFromFirebase(){
        auth.signOut()
    }
}