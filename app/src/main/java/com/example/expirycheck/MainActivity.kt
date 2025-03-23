package com.example.expirycheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expirycheck.navigation.App
import com.example.expirycheck.customs.ThemeMode
import com.example.expirycheck.ui.theme.ExpiryCheckTheme
import com.example.expirycheck.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val pvm: PreferencesViewModel = hiltViewModel()
            val selectedTheme by pvm.selectedTheme.collectAsState()

            val darkTheme = when (selectedTheme) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            ExpiryCheckTheme(darkTheme = darkTheme) {
                App()
            }
        }
    }
}
