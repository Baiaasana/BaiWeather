package com.example.baiweather.domain.model

import java.util.UUID

class ForecastData(
    val id: UUID = UUID.randomUUID(),
    val temp: Int,
    val maxTemp: Int? = 0,
    val icon: String,
    val time: String
)