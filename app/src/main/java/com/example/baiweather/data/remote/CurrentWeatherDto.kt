package com.example.baiweather.data.remote

import com.squareup.moshi.Json

data class CurrentWeatherDto(
    val cod: Int? = 0,
    val message: Int? = 0,
    val name: String? ="",  //city name
    val id: Int? = 0,
    val timezone: Float? = 0f,
    val weather: List<Weather>? = emptyList(),
    val main: WeatherObject? = WeatherObject(),
    val wind: Wind? = Wind(),
    val dt: Long? = 0L,
    val sys: SysInfo?  = SysInfo(),
    @field:Json(name = "coord")
    val coordinates: Coordinates? = Coordinates()
)

data class WeatherObject(
    val temp: Float? = 0f,
    @field:Json(name = "feels_like")
    val feelsLike: Float? = 0f,
    @field:Json(name = "temp_min")
    val tempMin: Float? = 0f,
    @field:Json(name = "temp_max")
    val tempMax: Float? = 0f,
    val pressure: Int? = 0,
    val humidity: Int? = 0
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Wind(
    val speed: Float?  =0f,
    val deg: Int? = 0,
    val gust: Float? = 0f
)

data class SysInfo(
    val country: String? = "",
    val sunrise: Long? = 0L,
    val sunset: Long? = 0L
)

data class Coordinates(
    val lon: Float? = 0f,
    val lat: Float? = 0f
)

