package com.example.weather

import android.app.Application

class WeatherApplication : Application() {

    val openWeatherMapApiKey: String
        get() = getString(R.string.openweathermap_api_key)
}