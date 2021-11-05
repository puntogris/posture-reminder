package com.puntogris.posture.model

import android.database.Cursor
import android.media.RingtoneManager
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ToneItem(val title: String, val uri: String) : Parcelable {

    companion object {
        fun from(cursor: Cursor): ToneItem {
            val id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX)
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX)
            return ToneItem(title, "$uri/$id")
        }
    }
}
