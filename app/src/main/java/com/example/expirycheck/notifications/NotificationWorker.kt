package com.example.expirycheck.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.expirycheck.R

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("NotificationWorker", "✅ Notification Worker started at ${System.currentTimeMillis()}")
        showNotification(applicationContext,12,0,5)
        return Result.success()
    }


    private fun showNotification(context: Context, expiredCount: Int, expiringTodayCount: Int, expiringTomorrowCount: Int) {
        val channelId = "daily_reminder_channel"
        val notificationId = 1001
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android 8.0+ (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Daily Reminder", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies about expiring items"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationText = buildNotificationText(expiredCount, expiringTodayCount, expiringTomorrowCount)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true) // Removes notification when clicked
            .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun buildNotificationText(expired: Int, expiringToday: Int, expiringTomorrow: Int): String {
        val parts = mutableListOf<String>()

        if (expired > 0) parts.add("$expired ${if (expired == 1) "item has" else "items have"} expired 🛑")
        if (expiringToday > 0) parts.add("$expiringToday ${if (expiringToday == 1) "item is" else "items are"} expiring today ⚠️")
        if (expiringTomorrow > 0) parts.add("$expiringTomorrow ${if (expiringTomorrow == 1) "item will" else "items will"} expire tomorrow ⏳")

        return if (parts.isNotEmpty()) parts.joinToString("\n") else "✅ No items are expiring soon!"
    }
}

