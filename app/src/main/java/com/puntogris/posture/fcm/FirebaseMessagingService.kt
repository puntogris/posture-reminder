package com.puntogris.posture.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.puntogris.posture.alarm.Notifications
import com.puntogris.posture.model.FcmNotification
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notifications: Notifications

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            notifications.sendFcmNotification(FcmNotification.from(remoteMessage))
        } catch (e: Exception) {
            //handle error
        }
    }

    override fun onNewToken(token: String) {
        // Log.d(ContentValues.TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token)
    }
}
