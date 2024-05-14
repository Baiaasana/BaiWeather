package com.example.baiweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baiweather.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class WeatherViewModel @Inject constructor(val weatherRepository: WeatherRepository) :ViewModel(){

    fun getCurrentWeather(){
        viewModelScope.launch {
            weatherRepository.getCurrentData(44.34,44.34)
        }
    }




}