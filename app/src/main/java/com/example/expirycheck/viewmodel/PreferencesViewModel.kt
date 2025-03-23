package com.example.expirycheck.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expirycheck.notifications.AppNotificationManager
import com.example.expirycheck.repository.PreferencesRepository
import com.example.expirycheck.customs.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _hour = MutableStateFlow(20) // Default 8:00 PM
    val hour = _hour.asStateFlow()

    private val _minute = MutableStateFlow(5) // Default 8:05 PM
    val minute = _minute.asStateFlow()

    private val _selectedTheme = MutableStateFlow(ThemeMode.SYSTEM) // Default mode
    val selectedTheme: StateFlow<ThemeMode> = _selectedTheme.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.themeMode.collect { theme ->
                _selectedTheme.value = theme
            }
        }
    }

    fun saveThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            preferencesRepository.saveThemeMode(themeMode)
            _selectedTheme.value = themeMode // ✅ Ensure UI updates
        }
    }

    fun saveNotificationTime(context: Context, hour: Int, minute: Int) {
        _hour.value = hour
        _minute.value = minute
        AppNotificationManager.scheduleDailyNotification(context, hour, minute)
    }
}
