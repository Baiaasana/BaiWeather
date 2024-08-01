package com.example.baiweather.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.baiweather.data.local.model.CityEntity

@Dao
interface CitiesDao {

    @Query("SELECT * FROM cities WHERE isFavourite is 1")
    fun getFavourites(): List<CityEntity>

    @Query("SELECT * FROM cities WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<CityEntity>

    @Query("UPDATE cities SET isFavourite = :isFavourite WHERE id = :cityId")
    fun setBookmark(isFavourite: Boolean, cityId: Int): Int

    @Query("SELECT * FROM cities WHERE city LIKE :city LIMIT 10")
    fun findByName(city: String): List<CityEntity>

    @Insert
    fun insertAll(cities: Array<CityEntity>)

    @Delete
    fun delete(user: CityEntity)
}