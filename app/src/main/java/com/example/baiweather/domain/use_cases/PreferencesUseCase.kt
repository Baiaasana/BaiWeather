package com.example.baiweather.domain.use_cases

import androidx.lifecycle.asLiveData
import com.example.baiweather.data.local.PreferencesDataStore

class PreferencesUseCase {

    val uiMode = PreferencesDataStore.darkModeFlow.asLiveData()
    suspend fun writeUiMode(isLightMode: Boolean) {
        PreferencesDataStore.saveDarkMode(isLightMode)
    }
}