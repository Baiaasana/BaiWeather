package com.example.baiweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baiweather.domain.location.LocationTracker
import com.example.baiweather.domain.use_cases.WeatherUseCase
import com.example.baiweather.domain.util.Resource
import com.example.baiweather.presentation.states.CurrentWeatherUiState
import com.example.baiweather.presentation.states.DailyWeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val locationTracker: LocationTracker,
) :
    ViewModel() {

    private val _currentWeatherState = MutableStateFlow(CurrentWeatherUiState())
    val currentWeatherState: StateFlow<CurrentWeatherUiState> = _currentWeatherState.asStateFlow()

    private val _dailyWeatherState = MutableStateFlow(DailyWeatherUiState())
    val dailyWeatherState: StateFlow<DailyWeatherUiState> = _dailyWeatherState.asStateFlow()

    fun getCurrentWeather() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                _currentWeatherState.emit(_currentWeatherState.value.copy(isLoading = true))
                val data = weatherUseCase.getCurrentData(location.latitude, location.longitude)
                when (data) {
                    is Resource.Error -> {
                        _currentWeatherState.emit(
                            _currentWeatherState.value.copy(
                                errorMessage = data.message.toString(),
                                isLoading = false
                            )
                        )
                    }

                    is Resource.Success -> {
                        _currentWeatherState.emit(
                            _currentWeatherState.value.copy(
                                data = data.data,
                                isLoading = false
                            )
                        )
                    }
                }
            }
        }
    }

    fun getDailyWeather() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                _dailyWeatherState.emit(_dailyWeatherState.value.copy(isLoading = true))
                val data = weatherUseCase.getDailyData(location.latitude, location.longitude, null)
                when (data) {
                    is Resource.Error -> {
                        _dailyWeatherState.emit(
                            _dailyWeatherState.value.copy(
                                errorMessage = data.message.toString(),
                                isLoading = false
                            )
                        )
                    }

                    is Resource.Success -> {
                        _dailyWeatherState.emit(
                            _dailyWeatherState.value.copy(
                                data = data.data,
                                isLoading = false
                            )
                        )
                    }
                }
            }
        }
    }
}