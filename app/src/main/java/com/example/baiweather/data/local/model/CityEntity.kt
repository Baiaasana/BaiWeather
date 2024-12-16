package com.example.baiweather.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "city")
    val name: String,
    @ColumnInfo(name = "country_code")
    val country: String,
    val isFavourite: Boolean? = false,
    @Embedded
    val coord: Coordinates
) {
    data class Coordinates(
        val lon: Double? = 0.0,
        val lat: Double? = 0.0
    )
}

