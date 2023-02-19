package pl.hamsterdev.pott

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.annotation.DrawableRes


class NotificationUtil (private val context: Context) {
    private val CHANNEL_ONE_ID = "pl.hamsterdev.pott.notification_channel_1"
    private val CHANNEL_ONE_NAME = "Push notification channel"

    private var notificationManager: NotificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(
        title: String,
        message: String,
        @DrawableRes icon: Int,
        notificationId: Int
    ) {
        createChannelIfNotExist(notificationManager)

        val notification: Notification = buildNotification(title, message, icon)
        notificationManager.notify(notificationId, notification)
    }

    private fun createChannelIfNotExist(notificationManager: NotificationManager) {
        if (notificationManager.getNotificationChannel(CHANNEL_ONE_ID) == null) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun buildNotification(
        title: String,
        message: String,
        @DrawableRes icon: Int
    ): Notification {
        return Notification.Builder(context, CHANNEL_ONE_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()
    }
}