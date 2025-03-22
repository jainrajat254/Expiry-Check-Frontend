package com.example.expirycheck.repository

import android.content.Context

class PreferencesRepository {

    enum class ThemeMode {
        SYSTEM, LIGHT, DARK
    }

    companion object {
        const val keyThemeMode = "theme_mode"
        private const val sharedPrefsName = "shared_pref"

        fun getThemeMode(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
        ): Int {
            try {
                return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .getInt(keyThemeMode, ThemeMode.SYSTEM.ordinal)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ThemeMode.SYSTEM.ordinal
        }

        fun setThemeMode(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
            themeMode: ThemeMode,
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit().putInt(keyThemeMode, themeMode.ordinal).apply()
        }
    }

}