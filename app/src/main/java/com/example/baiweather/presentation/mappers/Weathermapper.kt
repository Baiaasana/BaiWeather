package com.example.baiweather.presentation.mappers

import com.example.baiweather.common.Constants
import com.example.baiweather.data.remote.model.DailyWeatherDto
import com.example.baiweather.domain.model.AdditionalInfo
import com.example.baiweather.domain.model.ForecastData
import kotlin.math.roundToInt

fun DailyWeatherDto.toGridData(): List<AdditionalInfo> {
    val list = mutableListOf<AdditionalInfo>()
    this.list?.get(0).let { dailyData ->
        dailyData?.wind?.let {
            list.add(
                AdditionalInfo(
                    title = "Wind",
                    info = dailyData.wind.speed.toString(),
                )
            )
        }

        dailyData?.rain?.let {
            list.add(
                AdditionalInfo(
                    title = "Rain",
                    info = dailyData.rain.hours.toString()
                )
            )
        }
        dailyData?.main?.pressure?.let {
            list.add(
                AdditionalInfo(
                    title = "Pressure",
                    info = dailyData.main.pressure.toString()
                )
            )
        }
        dailyData?.main?.humidity?.let {
            list.add(
                AdditionalInfo(
                    title = "Humidity",
                    info = dailyData.main.humidity.toString()
                )
            )
        }

    }
    return list
}

fun DailyWeatherDto.toHourlyData(): List<ForecastData> {
    val list = mutableListOf<ForecastData>()
    this.list?.subList(0, 9)?.forEach {
        list.add(
            ForecastData(
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
