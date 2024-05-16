package com.example.baiweather.presentation.states

import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.data.remote.DailyWeatherDto

data class CurrentWeatherUiState(
    val isLoading: Boolean = false,
    val data: CurrentWeatherDto? = CurrentWeatherDto(),
    val errorMessage: String = "",
)

data class DailyWeatherUiState(
    val isLoading: Boolean = false,
    val data: DailyWeatherDto? = DailyWeatherDto(),
    val errorMessage: String = "",
)

