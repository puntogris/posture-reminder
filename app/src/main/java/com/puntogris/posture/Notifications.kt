package com.puntogris.posture

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.puntogris.posture.data.local.LocalDataSource
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.utils.Constants.CHANNEL_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Notifications @Inject constructor(@ApplicationContext private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val deprecatedChannels = listOf(
        "postureNotification"
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannelForReminderSdkO(reminder: Reminder){

        NotificationChannel(reminder.reminderId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).also {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            it.description = reminder.name
            it.enableVibration(true)
            it.enableLights(true)
            it.lightColor = Color.MAGENTA
            it.setSound(Uri.parse(reminder.soundUri), audioAttributes)

            if (reminder.soundUri.isNotBlank() && phoneIsInNormalMode()) {
                it.setSound(Uri.parse(reminder.soundUri), audioAttributes )
            }
            if (reminder.vibrationPattern != 0) {
                it.vibrationPattern = LocalDataSource().vibrationPatterns[reminder.vibrationPattern]
            }

            notificationManager.createNotificationChannel(it)
        }
    }

    fun getNotificationBuilderWithReminder(reminder: Reminder): NotificationCompat.Builder{
        val claimExpIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.navigation)
            .setDestination(R.id.claimNotificationExpDialog)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(context, reminder.reminderId)
            .setSmallIcon(R.drawable.ic_app_logo)
            .setContentTitle(context.getString(R.string.posture_notification_title))
            .setContentText(context.getString(R.string.posture_notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(claimExpIntent)
            .addAction(R.drawable.ic_baseline_add_24, context.getString(R.string.claim_exp_notification_action_title), claimExpIntent)
            .setAutoCancel(true)

        if (reminder.soundUri.isNotBlank() && phoneIsInNormalMode()) {
            builder.setSound(Uri.parse(reminder.soundUri))
        }

        if (reminder.vibrationPattern != 0) {
            builder.setVibrate(LocalDataSource().vibrationPatterns[reminder.vibrationPattern])
        }

        return builder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeDeprecatedChannels(){
        deprecatedChannels.forEach(notificationManager::deleteNotificationChannel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun removeNotificationChannelWithId(channelId: String){
        notificationManager.deleteNotificationChannel(channelId)
    }

    private fun phoneIsInNormalMode(): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.ringerMode == AudioManager.RINGER_MODE_NORMAL
    }

}