package com.puntogris.posture.data.remote

import javax.inject.Inject

class FirebaseLoginDataSource @Inject constructor(): FirebaseDataSource() {

    fun logOutFromFirebase(){
        auth.signOut()
    }
}