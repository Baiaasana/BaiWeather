package com.example.baiweather.data.repository

import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.data.remote.DailyWeatherDto
import com.example.baiweather.data.remote.WeatherApi
import com.example.baiweather.domain.repository.WeatherRepository
import com.example.baiweather.domain.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherApi: WeatherApi) : WeatherRepository {

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
    // max number is 40 - 5 day per 3 hours
    override suspend fun getDailyData(
        lat: Double,
        long: Double,
        cnt: Int?
    ): Resource<DailyWeatherDto> {
        return try {
            Resource.Success(
                data = weatherApi.getDailyWeather(lat = lat, lon = long, days = cnt)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Unknown error")
        }
    }

}