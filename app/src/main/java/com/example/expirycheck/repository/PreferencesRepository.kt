package com.example.expirycheck.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.expirycheck.customs.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesRepository(private val context: Context) {

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val HOUR_KEY = intPreferencesKey("notification_hour")
        private val MINUTE_KEY = intPreferencesKey("notification_minute")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        when (preferences[THEME_MODE_KEY]) {
            "LIGHT" -> ThemeMode.LIGHT
            "DARK" -> ThemeMode.DARK
            "SYSTEM" -> ThemeMode.SYSTEM
            else -> ThemeMode.SYSTEM
        }
    }

    suspend fun saveThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeMode.name
        }
    }

    suspend fun saveNotificationTime(hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[HOUR_KEY] = hour
            preferences[MINUTE_KEY] = minute
        }
    }

    suspend fun getNotificationTime(): Pair<Int, Int> {
        val preferences = context.dataStore.data.first()
        return Pair(preferences[HOUR_KEY] ?: 20, preferences[MINUTE_KEY] ?: 5)
    }
}

