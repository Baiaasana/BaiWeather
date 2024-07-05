package com.example.baiweather.domain.use_cases

import androidx.lifecycle.asLiveData
import com.example.baiweather.data.local.PreferencesDataStore
import javax.inject.Inject

class PreferencesUseCase @Inject constructor(
    private val preferencesDataStore:PreferencesDataStore
) {

    val uiMode = preferencesDataStore.darkModeFlow.asLiveData()
    suspend fun writeUiMode(isLightMode: Boolean) {
        preferencesDataStore.saveDarkMode(isLightMode)
    }
}