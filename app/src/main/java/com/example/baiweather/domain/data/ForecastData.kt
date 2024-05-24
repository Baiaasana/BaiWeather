package com.example.baiweather.domain.data

import java.util.UUID
import javax.annotation.processing.Generated

class ForecastData(
    val id: UUID = UUID.randomUUID(),
    val isHourly: Boolean,
    val temp: Int,
    val maxTemp: Int? = 0,
    val icon: String,
    val time: String
)