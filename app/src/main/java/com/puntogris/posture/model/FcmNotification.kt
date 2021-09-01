package com.puntogris.posture.model

import com.google.firebase.messaging.RemoteMessage

class FcmNotification(
    val uriString: String,
    val title: String,
    val message: String
){
    companion object{
        fun from(remoteMessage: RemoteMessage): FcmNotification{
            return FcmNotification(
                uriString = remoteMessage.data["uriString"].toString(),
                title = remoteMessage.notification?.title.toString(),
                message = remoteMessage.notification?.body.toString(),
            )
        }
    }
}