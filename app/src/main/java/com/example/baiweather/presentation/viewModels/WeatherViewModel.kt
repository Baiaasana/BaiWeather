package com.example.baiweather.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.data.remote.DailyWeatherDto
import com.example.baiweather.domain.location.LocationTracker
import com.example.baiweather.domain.use_cases.WeatherUseCase
import com.example.baiweather.domain.util.Resource
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
) : ViewModel() {

    private val _currentWeatherState =
        MutableStateFlow<Resource<CurrentWeatherDto>>(Resource.Loading)
    val currentWeatherState: StateFlow<Resource<CurrentWeatherDto>> =
        _currentWeatherState.asStateFlow()

    private val _dailyWeatherState = MutableStateFlow<Resource<DailyWeatherDto>>(Resource.Idle)
    val dailyWeatherState: StateFlow<Resource<DailyWeatherDto>> = _dailyWeatherState.asStateFlow()

    init {
        getCurrentWeather()
        getDailyWeather()
    }

    fun getCurrentWeather() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                val data = weatherUseCase.getCurrentData(location.latitude, location.longitude)
                _currentWeatherState.emit(data)
            }
        }
    }

    fun getDailyWeather() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                val data = weatherUseCase.getDailyData(location.latitude, location.longitude, null)
                _dailyWeatherState.emit(data)
            }
        }
    }
}
