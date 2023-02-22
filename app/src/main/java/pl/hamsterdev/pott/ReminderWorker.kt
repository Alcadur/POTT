package pl.hamsterdev.pott

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class ReminderWorker(val context: Context, val params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val itemName = inputData.getString(Consts.EXPIRE_NOTIFICATION_ITEM_NAME_PARAM)
        val itemAmount = inputData.getString(Consts.EXPIRE_NOTIFICATION_ITEM_QUANTITY_PARAM)
        val daysLeft = inputData.getString(Consts.EXPIRE_NOTIFICATION_DAYS_LEFT_PARAM)

        val title = context.resources.getString(R.string.item_expire_notification_title)
        val message = context.resources.getString(R.string.item_expire_notification_body, itemName, itemAmount, daysLeft)

        NotificationUtil(context).showNotification(title, message, Random.nextInt())

        return Result.success()
    }
}