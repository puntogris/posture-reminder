package com.puntogris.posture.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ToneItem(val title:String, val uri: String):Parcelable
