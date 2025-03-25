package com.example.expirycheck.notifications

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit
import java.util.Calendar

object AppNotificationManager {

    fun scheduleDailyNotification(context: Context, hour: Int, minute: Int) {
        Log.d("Notification", "Cancelling any existing work before scheduling.")
        WorkManager.getInstance(context).cancelUniqueWork("daily_notification")

        val delay = calculateInitialDelay(hour, minute)
        Log.d("Notification", "Scheduling notification for $hour:$minute with delay: $delay ms")

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_notification",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }


    private fun calculateInitialDelay(hour:Int, minute:Int): Long {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        if (calendar.timeInMillis <= currentTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Schedule for next day if time has passed
        }
        return calendar.timeInMillis - currentTime
    }
}
