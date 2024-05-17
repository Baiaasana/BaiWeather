package com.example.baiweather.data.remote

import com.example.baiweather.common.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(Constants.CURRENT_END_POINT)
    suspend fun getCurrentWeather(
        @Query(Constants.LAT) lat: Double, @Query(Constants.LON) lon: Double
    ): CurrentWeatherDto


    @GET(Constants.DAILY_END_POINT)
    suspend fun getDailyWeather(
        @Query(Constants.LAT) lat: Double, @Query(Constants.LON) lon: Double, @Query(Constants.CNT) days: Int ?= 40
    ): DailyWeatherDto
}