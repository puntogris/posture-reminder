package com.puntogris.posture.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.puntogris.posture.BuildConfig

@Keep
class Ticket (
    var uid: String = "",
    var username: String = "",
    var email: String = "",
    var message: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    var type: String = "",
    val buildCode : Int = BuildConfig.VERSION_CODE
){
    companion object{
        const val PROBLEM_TYPE =  "PROBLEM"
        const val SUGGESTION_TYPE = "SUGGESTION"
    }
}