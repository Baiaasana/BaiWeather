package com.example.baiweather.presentation.mappers

import com.example.baiweather.common.Constants
import com.example.baiweather.data.remote.DailyWeatherDto
import com.example.baiweather.domain.data.AdditionalInfo
import com.example.baiweather.domain.data.ForecastData
import kotlin.math.roundToInt

object DailyWeatherDto {

    fun DailyWeatherDto.toGridData(): List<AdditionalInfo> {
        val list = mutableListOf<AdditionalInfo>()
        this.list?.get(0).let { dailyData ->
            list.add(
                AdditionalInfo(
                    title = "Wind",
                    info = dailyData?.wind?.speed.toString(),
                )
            )
            list.add(
                AdditionalInfo(
                    title = "Rain",
                    info = dailyData?.rain?.hours.toString()
                )
            )
            list.add(
                AdditionalInfo(
                    title = "Pressure",
                    info = dailyData?.main?.pressure.toString()
                )
            )
            list.add(
                AdditionalInfo(
                    title = "Humidity",
                    info = dailyData?.main?.humidity.toString()
                )
            )
        }
        return list
    }

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