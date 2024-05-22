package com.example.baiweather.common

object Constants {

    const val PREFERENCES = "PREFERENCES"
    const val UI_MODE = "UI_MODE"

    const val BASE_URL = "https://api.openweathermap.org/"
    const val CURRENT_END_POINT =
        "data/2.5/weather?units=metric&appid=5a174ab46111459ed0d16f7c54803e6c"
    const val DAILY_END_POINT =
        "data/2.5/forecast?units=metric&appid=5a174ab46111459ed0d16f7c54803e6c"

    private const val ICON_TEMPLATE = "https://openweathermap.org/img/wn/%s@2x.png"

    fun getIconUrl(iconId: String): String {
        return ICON_TEMPLATE.format(iconId)
    }

    const val LAT = "lat"
    const val LON = "lon"
    const val CNT = "cnt"
}

