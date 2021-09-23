package com.puntogris.posture.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.puntogris.posture.utils.Constants
import com.puntogris.posture.utils.Constants.MAX_EXPERIENCE_OFFSET
import com.puntogris.posture.utils.Constants.MAX_EXPERIENCE_PER_DAY
import com.puntogris.posture.utils.toDays
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class UserPrivateData(

    @PrimaryKey(autoGenerate = false)
    @get:Exclude val roomId: Int =  1,

    @ColumnInfo
    val uid: String = "",

    @ColumnInfo
    val username: String = "",

    @ColumnInfo
    val country: String = "",

    @ColumnInfo
    val email: String = "",

    @ColumnInfo
    val photoUrl: String = "",

    @ColumnInfo
    val creationDate: Timestamp = Timestamp.now(),

    @ColumnInfo
    var experience: Int = 0,

    @ColumnInfo
    val currentReminderId: String = ""

):Parcelable{

    fun getMaxExpPermittedWithServerTimestamp(serverTimestamp: Long?): Int?{
        val creationTimestampMillis = creationDate.toDate().time

        return if (serverTimestamp != null) {
            val daysDiff = (serverTimestamp - creationTimestampMillis).toDays()

            val maxExpPermitted = daysDiff * MAX_EXPERIENCE_PER_DAY + MAX_EXPERIENCE_OFFSET

            if (experience > maxExpPermitted) maxExpPermitted else experience
        } else null

    }
}