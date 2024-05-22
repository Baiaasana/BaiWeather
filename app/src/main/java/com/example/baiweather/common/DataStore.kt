package com.example.baiweather.common

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.baiweather.BaiWeather.Companion.context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesSataStore {

    private val Context.dataStore by preferencesDataStore("settings")
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")


    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: true
        }

    suspend fun saveDarkMode(isLightMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isLightMode
        }
    }
}
