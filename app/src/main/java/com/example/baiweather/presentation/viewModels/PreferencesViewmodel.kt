package com.example.baiweather.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baiweather.domain.use_cases.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewmodel @Inject constructor(private val preferencesUseCase: PreferencesUseCase) : ViewModel() {

    val darkMode = preferencesUseCase.uiMode
    fun saveDarkMode(isLightMode: Boolean) {
        viewModelScope.launch {
            preferencesUseCase.writeUiMode(isLightMode)
        }
    }
}