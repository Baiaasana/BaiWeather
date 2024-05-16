package com.example.baiweather

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaiWeather: Application(){
    override fun onCreate() {
        super.onCreate()
            Timber.plant(Timber.DebugTree())

    }
}