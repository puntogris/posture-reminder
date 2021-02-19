package com.puntogris.posture.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
class Report (
        val username:String,
        val email:String,
        val message:String,
        val timestamp: Timestamp = Timestamp.now())