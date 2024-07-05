package com.example.baiweather.data.remote

data class CityJson(
    val id:Int,
    val name: String,
    val country: String,
    val coord: Coordinates
) {
    data class Coordinates(
        val lon: Double? = 0.0,
        val lat: Double? = 0.0
    )
}

