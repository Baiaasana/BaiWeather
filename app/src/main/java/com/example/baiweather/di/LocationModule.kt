package com.example.baiweather.di

import com.example.baiweather.data.location.DefaultLocationTracker
import com.example.baiweather.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Binds
    abstract fun bindLocationTracker(defaultLocationTracker: DefaultLocationTracker): LocationTracker

}