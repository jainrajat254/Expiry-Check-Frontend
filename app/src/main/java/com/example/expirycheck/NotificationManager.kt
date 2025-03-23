package com.example.expirycheck.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.expirycheck.NotificationReceiver
import java.util.Calendar

object AppNotificationManager {

    private var permissionGranted = false

    fun setupNotificationPermissions(activity: ComponentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionGranted = ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!permissionGranted) {
                val requestPermissionLauncher =
                    activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                        permissionGranted = isGranted
                        if (isGranted) {
                            Toast.makeText(activity, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
                        }
                    }
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun scheduleDailyNotification(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1) // If time has passed today, schedule for tomorrow
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }


        Toast.makeText(context, "Daily notification scheduled at $hour:$minute!", Toast.LENGTH_SHORT).show()
    }
}
