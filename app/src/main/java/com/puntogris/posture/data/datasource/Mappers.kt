package com.puntogris.posture.data.datasource

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.RemoteMessage
import com.puntogris.posture.domain.model.FcmNotification
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.model.UserPublicProfile
import com.puntogris.posture.utils.extensions.capitalizeWords
import com.puntogris.posture.utils.extensions.millisToTimestamp

fun FirebaseUser.toUserPrivateData(): UserPrivateData {
    val date = metadata?.creationTimestamp
    val timestamp = date?.millisToTimestamp() ?: Timestamp.now()

    return UserPrivateData(
        username = displayName.toString().capitalizeWords(),
        uid = uid,
        email = email.toString(),
        photoUrl = photoUrl.toString(),
        creationDate = timestamp
    )
}

fun UserPrivateData.toPublicProfile(): UserPublicProfile {
    return UserPublicProfile(
        username = username,
        country = country,
        uid = uid
    )
}

fun RemoteMessage.toFcmNotification(): FcmNotification {
    return FcmNotification(
        uriString = data["uriString"].toString(),
        title = notification?.title.toString(),
        message = notification?.body.toString(),
    )
}
