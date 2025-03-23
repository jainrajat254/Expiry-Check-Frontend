package com.example.expirycheck

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expirycheck.navigation.App
import com.example.expirycheck.notifications.AppNotificationManager
import com.example.expirycheck.ui.theme.ExpiryCheckTheme
import com.example.expirycheck.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        AppNotificationManager.setupNotificationPermissions(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }

        // Schedule a daily notification at 3:40 PM (15:40)
        AppNotificationManager.scheduleDailyNotification(this, 16, 9)

        setContent {
            val pvm: PreferencesViewModel = hiltViewModel()
            val darkTheme by pvm.isDarkMode.collectAsState()

            ExpiryCheckTheme(darkTheme = darkTheme) {
                App()
            }
        }
    }
}
