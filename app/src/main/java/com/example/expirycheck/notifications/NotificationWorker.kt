package com.example.expirycheck.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.expirycheck.MainActivity
import com.example.expirycheck.R
import com.example.expirycheck.repository.PreferencesRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("NotificationWorker", "✅ Notification Worker started at ${System.currentTimeMillis()}")

        val prefs = PreferencesRepository(applicationContext)
        val items = prefs.getItemsList()  // Already saved via HomeScreen
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)

        var expiredCount = 0
        var expiringTodayCount = 0
        var expiringTomorrowCount = 0

        for (item in items) {
            val date = parseDate(item.expiryDate)

            Log.d("NotificationWorker", "Item: ${item.itemName}, Expiry: ${item.expiryDate}, Parsed: $date")

            if (date != null) {
                when {
                    date.isBefore(today) -> expiredCount++
                    date.isEqual(today) -> expiringTodayCount++
                    date.isEqual(tomorrow) -> expiringTomorrowCount++
                }
            }
        }

        Log.d("NotificationWorker", "Expired: $expiredCount, Today: $expiringTodayCount, Tomorrow: $expiringTomorrowCount")

        showNotification(applicationContext, expiredCount, expiringTodayCount, expiringTomorrowCount)
        return Result.success()
    }

    private fun parseDate(dateStr: String): LocalDate? {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH)
            LocalDate.parse(dateStr, formatter)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }




    private fun showNotification(context: Context, expiredCount: Int, expiringTodayCount: Int, expiringTomorrowCount: Int) {
        val channelId = "daily_reminder_channel"
        val notificationId = 1001
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId, "Daily Reminder", NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notifies about expiring items"
        }
        notificationManager.createNotificationChannel(channel)

        val notificationText = buildNotificationText(expiredCount, expiringTodayCount, expiringTomorrowCount)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent) // ✅ Open app when tapped
            .setAutoCancel(true) // ✅ Removes notification when clicked
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

