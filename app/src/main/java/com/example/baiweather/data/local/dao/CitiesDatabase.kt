package com.example.baiweather.data.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.baiweather.data.local.model.CityEntity

@Database(entities = [CityEntity::class], version = 2, exportSchema = true)
abstract class CitiesDatabase : RoomDatabase() {

    abstract fun getCitiesDao(): CitiesDao

}