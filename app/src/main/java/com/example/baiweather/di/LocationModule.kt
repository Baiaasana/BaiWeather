package com.example.baiweather.di

import com.example.baiweather.data.location.DefaultLocationTracker
import com.example.baiweather.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(ViewModelComponent::class)
abstract class LocationModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Binds
    @ViewModelScoped
    abstract fun bindLocationTracker(defaultLocationTracker: DefaultLocationTracker): LocationTracker

}