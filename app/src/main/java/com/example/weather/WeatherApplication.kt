package com.example.weather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication : Application() {

    val openWeatherMapApiKey: String
        get() = getString(R.string.openweathermap_api_key)
}