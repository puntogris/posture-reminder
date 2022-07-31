package com.puntogris.posture.domain.model

import android.os.Build
import com.google.firebase.Timestamp
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.utils.IDGenerator

class Ticket(
    val id: String = IDGenerator.randomID(),
    val uid: String,
    val message: String,
    val versionName: String = BuildConfig.VERSION_NAME,
    val versionCode: Int = BuildConfig.VERSION_CODE,
    val sdkVersion: Int = Build.VERSION.SDK_INT,
    val releaseVersion: String = Build.VERSION.RELEASE,
    val deviceBrand: String = Build.BRAND,
    val deviceModel: String = Build.MODEL,
    val deviceManufacturer: String = Build.MANUFACTURER,
    val timestamp: Timestamp = Timestamp.now()
)
