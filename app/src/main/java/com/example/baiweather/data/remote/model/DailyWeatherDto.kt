package com.example.baiweather.data.remote.model

import com.squareup.moshi.Json

data class DailyWeatherDto(
    val cod: Int? = 0,
    val message: Int? = 0,
    val cnt: Int? = 0,
    val list: List<DailyData>? = emptyList(),
    val city: City? = City()
)

data class City(
    val id: Int? = 0,
    val name: String? = "",
    val country: String? = "",
    val sunrise: Long? = 0L,
    val sunset: Long? = 0L,
    @field:Json(name = "coord")
    val coordinates: Coordinates? = null,
    val timezone: Int? = 0
)

data class DailyData(
    val dt: Long? = 0L,
    val main: WeatherObject? = WeatherObject(),
    val weather: List<Weather>? = emptyList(),
    val wind: Wind? = null,
    val visibility: Int? = 0,
    val rain: Rain? = null,
    val sys: Sys? = Sys(),
    val clouds: Clouds? = Clouds(),
    @field:Json(name = "dt_txt")
    val dateText: String? = "",
    )

data class Rain(
    @field:Json(name = "3h")
    val hours: Float? = 0f
)

data class Clouds(
    val all: Int? = 0
)

data class Sys(
    val pod: String? = ""
)

