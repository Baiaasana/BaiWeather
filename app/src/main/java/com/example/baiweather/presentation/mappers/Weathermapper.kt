package com.example.baiweather.presentation.mappers

import com.example.baiweather.common.Constants
import com.example.baiweather.data.remote.DailyWeatherDto
import com.example.baiweather.domain.data.ForecastData
import kotlin.math.roundToInt

object DailyWeatherDto {
    fun DailyWeatherDto.toHourlyData(): List<ForecastData> {
        val list = mutableListOf<ForecastData>()
        this.list?.subList(0, 9)?.forEach {
            list.add(
                ForecastData(
                    isHourly = true,
                    temp = it.main?.temp!!.roundToInt(),
                    time = it.dateText.toString()
                        .substring(it.dateText!!.length - 9, it.dateText.length - 3),
                    icon = Constants.getIconUrl(it.weather?.get(0)?.icon.toString()),
                    maxTemp = null
                )
            )
        }
        return list
    }

    fun DailyWeatherDto.toForecastData(): List<ForecastData> {
        val list = mutableListOf<ForecastData>()
        this.list?.forEachIndexed { index, it ->
            if (index % 8 == 0) {
                list.add(
                    ForecastData(
                        isHourly = false,
                        temp = it.main?.tempMin!!.roundToInt(),
                        time = it.dateText.toString().substring(5, 10).replace("-", "/"),
                        icon = Constants.getIconUrl(it.weather?.get(0)?.icon.toString()),
                        maxTemp = it.main.tempMax?.roundToInt()
                    )
                )
            }
        }
        return list
    }
}