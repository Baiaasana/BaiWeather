package com.example.baiweather.data.remote

import com.example.baiweather.common.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(Constants.CURRENT_END_POINT)
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): CurrentWeatherDto

    @GET(Constants.HOURLY_END_POINT)
    suspend fun getHourlyWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): CurrentWeatherDto

    @GET(Constants.DAILY_END_POINT)
    suspend fun getDailyWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double, @Query("cnt") cnt: Int
    ): CurrentWeatherDto
}