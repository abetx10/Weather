package com.example.weather.domain.model

class CityCurrentWeatherData(
    name: String,
    lat: Double,
    lon: Double,
    val temp: Double,
    val pressure: Int,
    val windSpeed: Double
) : CityData(name, lat, lon)