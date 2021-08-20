package com.puntogris.posture.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
class Ticket (
    var uid: String = "",
    var username:String = "",
    var email:String = "",
    var message:String = "",
    val timestamp: Timestamp = Timestamp.now(),
    var type: String = ""
)