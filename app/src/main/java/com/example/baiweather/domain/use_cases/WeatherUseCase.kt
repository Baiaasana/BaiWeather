package com.example.baiweather.domain.use_cases

import android.util.Log
import com.example.baiweather.data.remote.model.CurrentWeatherDto
import com.example.baiweather.data.remote.model.DailyWeatherDto
import com.example.baiweather.domain.repository.WeatherRepository
import com.example.baiweather.domain.util.Resource
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {

    suspend fun getCurrentData(lat: Double, long: Double): Resource<CurrentWeatherDto> {
        Log.d("setOnMarkerClickListener", " use case ${lat}  ${long} ")
        return weatherRepository.getCurrentData(lat, long)
    }

    suspend fun getDailyData(lat: Double, long: Double, cnt: Int?): Resource<DailyWeatherDto> {
        return weatherRepository.getDailyData(lat, long, cnt)
    }
}