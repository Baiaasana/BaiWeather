package com.example.baiweather.domain.repository

import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.data.remote.WeatherInfo
import com.example.baiweather.domain.util.Resource
import com.example.baiweather.domain.weather.WeatherData

interface WeatherRepository {

    suspend fun getCurrentData(lat: Double, long: Double): Resource<CurrentWeatherDto>

    suspend fun getHourlyData(lat: Double, long: Double): Resource<WeatherData>

    suspend fun getWeeklyData(lat: Double, long: Double): Resource<WeatherData>


}