package com.example.baiweather.domain.data

class ForecastData(
    val isHourly: Boolean,
    val temp: Int,
    val maxTemp: Int? = 0,
    val icon: String,
    val time: String
)