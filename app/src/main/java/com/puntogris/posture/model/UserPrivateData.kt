package com.puntogris.posture.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class UserPrivateData(

    @PrimaryKey(autoGenerate = false)
    @get:Exclude val roomId: Int =  1,

    @ColumnInfo
    val id: String,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val country: String = "",

    @ColumnInfo
    val email: String = "",

    @ColumnInfo
    val photoUrl: String = "",

    @ColumnInfo
    val creationDate: Timestamp = Timestamp.now(),

    @ColumnInfo
    @get:Exclude val experience: Int = 0,

    @ColumnInfo
    val currentReminderId: String = ""

):Parcelable