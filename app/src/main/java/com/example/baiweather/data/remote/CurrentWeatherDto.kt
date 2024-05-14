package com.example.baiweather.data.remote

import com.squareup.moshi.Json

data class CurrentWeatherDto(
    val cod: Int,
    val message: Int,
    val name: String,
    val id: Int,
    val timezone: Float,
    val list: List<WeatherInfo>,
    @field:Json(name = "coord")
    val coordinates: Coordinates
)

data class WeatherInfo(
    val main: WeatherObject,
    val weather: List<Weather>,
    val pressure: Int,
    val humidity: Int,
    val wind: Wind,
    val visibility: Int,
    val dt: Long,
    val sys: SysInfo
)

data class WeatherObject(
    val temp: Float,
    @field:Json(name = "feels_like")
    val feelsLike: Float,
    @field:Json(name = "temp_min")
    val tempMin: Float,
    @field:Json(name = "temp_max")
    val tempMax: Float,
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Float,
    val deg: Int
)

data class SysInfo(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class Coordinates(
    val lon: Float,
    val lat: Float
)

