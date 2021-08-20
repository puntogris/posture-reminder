package com.puntogris.posture.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToneItem(val title:String, val uri: String):Parcelable
