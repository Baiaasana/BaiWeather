package com.example.baiweather.domain.use_cases

import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.data.remote.DailyWeatherDto
import com.example.baiweather.domain.repository.WeatherRepository
import com.example.baiweather.domain.util.Resource
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun getCurrentData(lat: Double, long: Double): Resource<CurrentWeatherDto> {
        return weatherRepository.getCurrentData(lat, long)
    }

    suspend fun getDailyData(lat: Double, long: Double, cnt: Int?): Resource<DailyWeatherDto> {
        return weatherRepository.getDailyData(lat, long, cnt)
    }

}