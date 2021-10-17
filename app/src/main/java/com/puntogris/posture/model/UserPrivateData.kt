package com.puntogris.posture.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Exclude
import com.puntogris.posture.utils.Constants.BASE_DATE_MILLIS
import com.puntogris.posture.utils.Constants.MAX_EXPERIENCE_PER_DAY
import com.puntogris.posture.utils.capitalizeWords
import com.puntogris.posture.utils.toDays
import kotlinx.parcelize.Parcelize
import java.util.*

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

        return if (serverTimestamp != null) {
            val daysDiff = (serverTimestamp - BASE_DATE_MILLIS).toDays()
            val maxExpPermitted = daysDiff * MAX_EXPERIENCE_PER_DAY

            if (experience > maxExpPermitted) maxExpPermitted else experience
        } else null
    }

    companion object {
        fun from(user: FirebaseUser?): UserPrivateData{
            val date = user?.metadata?.creationTimestamp
            val timestamp = if (date == null) Timestamp.now() else Timestamp((Date(date)))

            return UserPrivateData(
                username = user?.displayName.toString().capitalizeWords(),
                uid = user?.uid.toString(),
                email = user?.email.toString(),
                photoUrl = user?.photoUrl.toString(),
                creationDate = timestamp
            )
        }
    }
}