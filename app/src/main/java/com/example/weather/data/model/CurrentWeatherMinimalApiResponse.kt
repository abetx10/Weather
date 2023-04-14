package com.example.weather.data.model

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