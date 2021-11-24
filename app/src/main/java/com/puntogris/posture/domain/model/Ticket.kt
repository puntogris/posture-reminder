package com.puntogris.posture.domain.model

import android.os.Build
import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.puntogris.posture.BuildConfig

@Keep
class Ticket(

    var uid: String = "",

    var username: String = "",

    var email: String = "",

    var message: String = "",

    val timestamp: Timestamp = Timestamp.now(),

    var type: String = "",

    val versionCode: Int? = BuildConfig.VERSION_CODE,

    val device: String? = Build.DEVICE,

    val deviceModel: String? = Build.MODEL,

    val androidSdk: Int = Build.VERSION.SDK_INT,

    val androidRelease: String? = Build.VERSION.RELEASE

) {
    companion object {
        const val PROBLEM_TYPE = "PROBLEM"
        const val SUGGESTION_TYPE = "SUGGESTION"
    }
}