package com.example.weather.data.api

data class CurrentWeatherMinimalApiResponse(
    val main: MainWeatherMinimalData,
    val wind: WindMinimalData
)

data class MainWeatherMinimalData(
    val temp: Double,
    val pressure: Int
)

data class WindMinimalData(
    val speed: Double
)