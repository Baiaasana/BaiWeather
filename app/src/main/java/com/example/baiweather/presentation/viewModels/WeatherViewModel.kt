package com.example.baiweather.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
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
        MutableStateFlow<Resource<CurrentWeatherDto>>(Resource.Idle)
    val currentWeatherState: StateFlow<Resource<CurrentWeatherDto>> =
        _currentWeatherState.asStateFlow()

    private val _cityWeatherState =
        MutableStateFlow<Resource<CurrentWeatherDto>>(Resource.Idle)
    val cityWeatherState: StateFlow<Resource<CurrentWeatherDto>> =
        _cityWeatherState.asStateFlow()

    private val _dailyWeatherState = MutableStateFlow<Resource<DailyWeatherDto>>(Resource.Idle)
    val dailyWeatherState: StateFlow<Resource<DailyWeatherDto>> = _dailyWeatherState.asStateFlow()

    private lateinit var dataValue: LiveData<String>
    private lateinit var dataValue2: LiveData<String>

//    Use map() when you want to transform a value emitted by a LiveData into a non-LiveData result.
//    Use switchMap() when you need to switch to a different LiveData based on a trigger LiveData.

    // just test livedata
    private fun onGetNumber() {
        dataValue = weatherUseCase.getLiveData().map { str ->
            str.plus(str)
        }

        dataValue2 = weatherUseCase.getLiveData().switchMap {
            weatherUseCase.getLiveData2(it)
        }
    }

    fun onFragmentReady() {
        onGetNumber()
        getCurrentWeather()
        getDailyWeather()
    }

    private fun getCurrentWeather() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                _currentWeatherState.emit(Resource.Loading)
                val data = weatherUseCase.getCurrentData(location.latitude, location.longitude)
                _currentWeatherState.emit(data)
            }
        }
    }

    private fun getDailyWeather() {
        viewModelScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                _dailyWeatherState.emit(Resource.Loading)
                val data = weatherUseCase.getDailyData(location.latitude, location.longitude, null)
                _dailyWeatherState.emit(data)
            }
        }
    }

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            _cityWeatherState.emit(Resource.Loading)
            val data = weatherUseCase.getCurrentWeatherByCity(city)
            _cityWeatherState.emit(data)
        }
    }
}
