package com.example.expirycheck.notifications

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import java.util.Calendar

object AppNotificationManager {

    fun scheduleDailyNotification(context: Context, hour: Int, minute: Int) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelay(hour = hour, minute = minute), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_notification",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun calculateInitialDelay(hour:Int, minute:Int): Long {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour) // 16:09
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        if (calendar.timeInMillis <= currentTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Schedule for next day if time has passed
        }
        return calendar.timeInMillis - currentTime
    }
}
