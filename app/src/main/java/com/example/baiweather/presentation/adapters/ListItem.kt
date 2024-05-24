package com.example.baiweather.presentation.adapters

import com.example.baiweather.domain.data.AdditionalInfo
import com.example.baiweather.domain.data.ForecastData

sealed class ListItem {
    data class Horizontal(val id:Int, val title: String, val items: List<ForecastData>) : ListItem()
    data class Grid(val id:Int, val title: String, val items: List<AdditionalInfo>) : ListItem()
    data class Vertical(val id:Int, val title: String, val items: List<ForecastData>) : ListItem()

}