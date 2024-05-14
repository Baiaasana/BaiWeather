package com.example.baiweather.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

//    https://pro.openweathermap.org/data/2.5/forecast/hourly?lat={lat}&lon={lon}&appid={API key}
//
//    https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
//
//    https://api.openweathermap.org/data/2.5/forecast/daily?lat={lat}&lon={lon}&cnt={cnt}&appid={API key}


    @GET("data/2.5/weather?appid=385fb0fae782ddd4bfe16a4c8ee131ac")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): CurrentWeatherDto

    @GET("data/2.5/forecast/hourly?appid=385fb0fae782ddd4bfe16a4c8ee131ac")
    suspend fun getHourlyWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): CurrentWeatherDto

    @GET("data/2.5/forecast/daily?appid=385fb0fae782ddd4bfe16a4c8ee131ac")
    suspend fun getDailyWeather(
        @Query("lat") lat: Double, @Query("lon") lon: Double, @Query("cnt") cnt: Int
    ): CurrentWeatherDto
}