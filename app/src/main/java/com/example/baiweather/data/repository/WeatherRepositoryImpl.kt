package com.example.baiweather.data.repository

import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.data.remote.WeatherApi
import com.example.baiweather.domain.repository.WeatherRepository
import com.example.baiweather.domain.util.Resource
import com.example.baiweather.domain.weather.WeatherData
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(val weatherApi: WeatherApi) : WeatherRepository {

    override suspend fun getCurrentData(
        lat: Double,
        long: Double
    ): Resource<CurrentWeatherDto> {
        return try {
            Resource.Success(
                data = weatherApi.getCurrentWeather(lat = lat, lon = long)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Unknown error")
        }
    }

    override suspend fun getHourlyData(lat: Double, long: Double): Resource<WeatherData> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeeklyData(lat: Double, long: Double): Resource<WeatherData> {
        TODO("Not yet implemented")
    }
}