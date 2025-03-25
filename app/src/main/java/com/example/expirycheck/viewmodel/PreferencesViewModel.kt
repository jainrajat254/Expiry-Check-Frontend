package com.example.expirycheck.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expirycheck.customs.ThemeMode
import com.example.expirycheck.notifications.AppNotificationManager
import com.example.expirycheck.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _hour = MutableStateFlow(11)
    val hour = _hour.asStateFlow()

    private val _minute = MutableStateFlow(0)
    val minute = _minute.asStateFlow()

    private val _selectedTheme = MutableStateFlow(ThemeMode.SYSTEM) // Default mode
    val selectedTheme: StateFlow<ThemeMode> = _selectedTheme.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.themeMode.collect { theme ->
                _selectedTheme.value = theme
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val (savedHour, savedMinute) = preferencesRepository.getNotificationTime()
            _hour.value = savedHour
            _minute.value = savedMinute
        }
    }

    fun saveThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.saveThemeMode(themeMode)
            _selectedTheme.value = themeMode
        }
    }

    fun saveNotificationTime(context: Context, hour: Int, minute: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _hour.value = hour
            _minute.value = minute
            preferencesRepository.saveNotificationTime(hour, minute) // ✅ Save to DataStore
            withContext(Dispatchers.Main) {
                AppNotificationManager.scheduleDailyNotification(context, hour, minute) // ✅ Run on Main Thread (UI-related)
            }
        }
    }
}
