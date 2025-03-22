package com.example.expirycheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expirycheck.navigation.App
import com.example.expirycheck.repository.PreferencesRepository
import com.example.expirycheck.ui.theme.ExpiryCheckTheme
import com.example.expirycheck.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val prefsViewModel: PreferencesViewModel = viewModel()
            val darkThemeState = prefsViewModel.getThemeMode(context).collectAsState().value
            val isInDarkTheme = when (darkThemeState) {
                PreferencesRepository.ThemeMode.LIGHT.ordinal -> false
                PreferencesRepository.ThemeMode.DARK.ordinal -> true
                else -> isSystemInDarkTheme()
            }

            ExpiryCheckTheme(darkTheme = isInDarkTheme) {
                App()
            }
        }
    }
}

