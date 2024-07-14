package com.example.baiweather.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.baiweather.common.Constants.DARK_MODE
import com.example.baiweather.common.Constants.SETTINGS
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataStore @Inject constructor(
    @ApplicationContext val context: Context
){

    private val Context.dataStore by preferencesDataStore(SETTINGS)
    private val DARK_MODE_KEY = booleanPreferencesKey(DARK_MODE)

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
