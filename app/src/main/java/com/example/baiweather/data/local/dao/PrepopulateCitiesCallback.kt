package com.example.baiweather.data.local.dao

import android.content.Context
import android.util.JsonReader
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.baiweather.data.local.model.CityEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class PrepopulateCitiesCallback(private val db: CitiesDatabase, private val context: Context) :
    RoomDatabase.Callback() {

    private lateinit var cities: Array<CityEntity>

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prePopulateCities(context)
        }
    }

    private suspend fun prePopulateCities(context: Context) {
        try {
            val citiesDao = db.getCitiesDao()
            try {
                cities = withContext(Dispatchers.IO) {
                    readJsonStream(context.assets.open("city.list.json"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            citiesDao.insertAll(cities = cities)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun readJsonStream(stream: InputStream?): Array<CityEntity> {
        val reader = JsonReader(InputStreamReader(stream, "UTF-8"))
        try {
            return readCitiesArray(reader)
        } finally {
            reader.close()
        }
    }

    @Throws(IOException::class)
    fun readCitiesArray(reader: JsonReader): Array<CityEntity> {
        val cities: ArrayList<CityEntity> = ArrayList<CityEntity>()
        reader.beginArray()
        while (reader.hasNext()) {
            cities.add(readCity(reader))
        }
        reader.endArray()
        return cities.toTypedArray()
    }

    @Throws(IOException::class)
    fun readCity(reader: JsonReader): CityEntity {
        var id: Int = -1
        var cityName: String? = ""
        var country: String? = ""
        var coord: CityEntity.Coordinates? = CityEntity.Coordinates()

        reader.beginObject()
        while (reader.hasNext()) {
            val name: String = reader.nextName()
            when (name) {
                "id" -> {
                    id = reader.nextInt()
                }

                "name" -> {
                    cityName = reader.nextString()
                }

                "country" -> {
                    country = reader.nextString()
                }

                "coord" -> {
                    coord = readCoordinates(reader)
                }
                else -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        return CityEntity(id, cityName ?: "", country ?: "", coord ?: CityEntity.Coordinates())
    }

    @Throws(IOException::class)
    fun readCoordinates(reader: JsonReader): CityEntity.Coordinates {
        var lon: Double? = null
        var lat: Double? = null

        reader.beginObject()
        while (reader.hasNext()) {
            val name: String = reader.nextName()
            if (name == "lon") {
                lon = reader.nextDouble()
            } else if (name == "lat") {
                lat = reader.nextDouble()
            } else {
                reader.skipValue()
            }
        }
        reader.endObject()
        return CityEntity.Coordinates(lon, lat)
    }
}