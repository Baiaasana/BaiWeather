package com.example.baiweather.domain.use_cases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    suspend fun getCurrentWeatherByCity(city: String): Resource<CurrentWeatherDto> {
        return weatherRepository.getCurrentDataByCity(city)
    }


    // LiveData
    val liveData = MutableLiveData<String>("data")

    fun getLiveData(): LiveData<String> {
        return liveData
    }

    fun getLiveData2(str: String): LiveData<String> {
        return MutableLiveData(str.plus("404"))
    }

}