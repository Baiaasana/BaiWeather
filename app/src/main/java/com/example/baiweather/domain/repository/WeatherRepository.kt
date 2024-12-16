package com.example.baiweather.domain.repository

import com.example.baiweather.data.remote.model.CurrentWeatherDto
import com.example.baiweather.data.remote.model.DailyWeatherDto
import com.example.baiweather.domain.util.Resource

interface WeatherRepository {

    suspend fun getCurrentData(lat: Double, long: Double): Resource<CurrentWeatherDto>

    suspend fun getDailyData(lat: Double, long: Double, cnt: Int? = null): Resource<DailyWeatherDto>

    suspend fun getCurrentDataByCity(city: String): Resource<CurrentWeatherDto>


}