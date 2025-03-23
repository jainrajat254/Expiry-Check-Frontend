package com.example.expirycheck.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.expirycheck.R

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification(applicationContext)
        return Result.success()
    }

    private fun showNotification(context: Context) {
        val channelId = "daily_reminder_channel"
        val notificationId = 1001

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Daily Reminder", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder")
            .setContentText("Check your expiry items today!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()

        notificationManager.notify(notificationId, notification)
    }
}

