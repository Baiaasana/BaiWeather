package com.example.baiweather.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.baiweather.data.local.dao.CitiesDao
import com.example.baiweather.data.local.dao.CitiesDatabase
import com.example.baiweather.data.local.dao.PrepopulateCitiesCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CitiesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CitiesDatabase::class.java,
            "cities_database"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        PrepopulateCitiesCallback(
                            provideDatabase(context),
                            context
                        ).onCreate(db)
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCitiesDao(appDatabase: CitiesDatabase): CitiesDao {
        return appDatabase.getCitiesDao()
    }
}