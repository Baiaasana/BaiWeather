package com.example.baiweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.baiweather.common.PreferencesSataStore
import kotlinx.coroutines.launch

class DataStoreViewModel : ViewModel() {

    val darkMode = PreferencesSataStore.darkModeFlow.asLiveData()
    fun saveDarkMode(isLightMode: Boolean) {
        viewModelScope.launch {
            PreferencesSataStore.saveDarkMode(isLightMode)
        }
    }
}