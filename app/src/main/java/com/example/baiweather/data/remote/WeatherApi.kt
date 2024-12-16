package com.example.baiweather.data.remote

import com.example.baiweather.common.Constants
import com.example.baiweather.data.remote.model.CurrentWeatherDto
import com.example.baiweather.data.remote.model.DailyWeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET(Constants.CURRENT_END_POINT)
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): CurrentWeatherDto

    @GET(Constants.DAILY_END_POINT)
    suspend fun getDailyWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double, @Query("cnt") days: Int ?= 40
    ): DailyWeatherDto

    @GET(Constants.CURRENT_END_POINT_BY_CITY)
    suspend fun getCurrentWeatherByCity(
        @Query("q") city: String
    ): CurrentWeatherDto
}