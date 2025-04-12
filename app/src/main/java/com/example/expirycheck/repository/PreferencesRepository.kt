package com.example.expirycheck.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.expirycheck.customs.ThemeMode
import com.example.expirycheck.models.Items
import com.example.expirycheck.models.LoginResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesRepository(private val context: Context) {

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
        private val HOUR_KEY = intPreferencesKey("notification_hour")
        private val MINUTE_KEY = intPreferencesKey("notification_minute")

        private val FULL_NAME = stringPreferencesKey("full_name")
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val ITEMS = stringPreferencesKey("items")
    }

    private val gson = Gson()

    // Save list of items
    suspend fun saveItemsList(itemList: List<Items>) {
        val json = gson.toJson(itemList)
        Log.d("DataStore", "Serialized items: $json")  // Log the JSON for debugging
        context.dataStore.edit { preferences ->
            preferences[ITEMS] = json
        }
    }

    suspend fun removeItemFromList(itemId: String) {
        val itemList = getItemsList()
        val updatedItemList = itemList.filter { it.id != itemId }
        saveItemsList(updatedItemList)
    }

    // Get list of items
    suspend fun getItemsList(): List<Items> {
        val preferences = context.dataStore.data.first()
        val json = preferences[ITEMS] ?: return emptyList()
        Log.d("DataStore", "Loaded JSON from DataStore: $json")  // Log the JSON string
        val type = object : TypeToken<List<Items>>() {}.type
        val itemList: List<Items> = gson.fromJson(json, type)  // Parse the JSON to a List<Items>
        itemList.forEachIndexed { index, item ->
            Log.d(
                "DataStore",
                "Item $index: $item (id: ${item.id})"
            )  // Check if id is populated correctly
        }
        return itemList
    }

    // Save user
    suspend fun saveUser(loginResponse: LoginResponse) {
        val itemsJson = gson.toJson(loginResponse.items)
        context.dataStore.edit { preferences ->
            preferences[FULL_NAME] = loginResponse.fullName
            preferences[USERNAME] = loginResponse.username
            preferences[EMAIL] = loginResponse.username
            preferences[ITEMS] = itemsJson
        }
    }

    fun isUserLoggedIn(): Boolean {
        return runBlocking {
            val username = getUsername()
            !username.isNullOrEmpty()  // If username is not null or empty, user is logged in
        }
    }

    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(FULL_NAME)
            preferences.remove(USERNAME)
            preferences.remove(EMAIL)
            preferences.remove(ITEMS)
        }
    }


    suspend fun getUsername(): String? {
        return context.dataStore.data
            .map { preferences ->
                preferences[USERNAME]
            }
            .firstOrNull()
    }

    // Theme Mode
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

    // Notification time
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
