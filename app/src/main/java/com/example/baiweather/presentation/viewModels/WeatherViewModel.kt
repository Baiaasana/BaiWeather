package com.example.baiweather.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baiweather.data.remote.model.CurrentWeatherDto
import com.example.baiweather.data.remote.model.DailyWeatherDto
import com.example.baiweather.domain.location.LocationTracker
import com.example.baiweather.domain.use_cases.WeatherUseCase
import com.example.baiweather.domain.util.Resource
import com.google.android.gms.maps.model.LatLng
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
        MutableStateFlow<Resource<CurrentWeatherDto>>(Resource.Idle)
    val currentWeatherState: StateFlow<Resource<CurrentWeatherDto>> =
        _currentWeatherState.asStateFlow()

    private val _cityWeatherState =
        MutableStateFlow<Resource<CurrentWeatherDto>>(Resource.Idle)
    val cityWeatherState: StateFlow<Resource<CurrentWeatherDto>> =
        _cityWeatherState.asStateFlow()

    private val _dailyWeatherState = MutableStateFlow<Resource<DailyWeatherDto>>(Resource.Idle)
    val dailyWeatherState: StateFlow<Resource<DailyWeatherDto>> = _dailyWeatherState.asStateFlow()

    fun onFragmentReady() {
        getCurrentWeather(null)
        getDailyWeather(null)
    }

    init {
        getCurrentWeather(null)
        getDailyWeather(null)
    }

    fun getCurrentWeather(latLng: LatLng?) {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                _currentWeatherState.emit(Resource.Loading)
                val data = if (latLng == null) weatherUseCase.getCurrentData(
                    location.latitude,
                    location.longitude
                )
                else {
                    weatherUseCase.getCurrentData(latLng.latitude, latLng.longitude)
                }
                _currentWeatherState.emit(data)
            }
        }
    }

    fun getDailyWeather(latLng: LatLng?) {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                _dailyWeatherState.emit(Resource.Loading)
                val data = if (latLng == null) {
                    weatherUseCase.getDailyData(location.latitude, location.longitude, null)
                } else {
                    weatherUseCase.getDailyData(latLng.latitude, latLng.longitude, null)
                }
                _dailyWeatherState.emit(data)
            }
        }
    }

    fun getWeatherByCity(latLng: LatLng) {
        viewModelScope.launch {
            _cityWeatherState.emit(Resource.Loading)
            val data = weatherUseCase.getCurrentData(latLng.latitude, latLng.longitude)
            _cityWeatherState.emit(data)
        }
    }
}
