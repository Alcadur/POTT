package pl.hamsterdev.pott

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


class NotificationUtil (private val context: Context) {
    private val CHANNEL_ONE_ID = "pl.hamsterdev.pott.notification_channel_1"
    private val CHANNEL_ONE_NAME = "Push notification channel"
    private val icon: Int = R.drawable.ic_launcher_foreground

    private var notificationManager: NotificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(
        title: String,
        message: String,
        notificationId: Int
    ) {
        createChannelIfNotExist(notificationManager)

        val notification: Notification = buildNotification(title, message)
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

    fun scheduleExpireNotification(item: ItemModel, delayInSeconds: Long) {
        val workRequestWorker = OneTimeWorkRequestBuilder<ReminderWorker>()
            .addTag(item.id)

        val workerData = Data.Builder()
            .putString(Consts.EXPIRE_NOTIFICATION_ITEM_NAME_PARAM, item.name)
            .putString(Consts.EXPIRE_NOTIFICATION_ITEM_QUANTITY_PARAM, item.quantity.toString())
            .putString(Consts.EXPIRE_NOTIFICATION_ITEM_QUANTITY_PARAM, item.duration.toDays().toString())
            .build()

        workRequestWorker.setInitialDelay(delayInSeconds, TimeUnit.SECONDS).setInputData(workerData)

        WorkManager.getInstance(context).enqueue(workRequestWorker.build())
    }

    private fun buildNotification(
        title: String,
        message: String
    ): Notification {
        return Notification.Builder(context, CHANNEL_ONE_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .build()
    }
}