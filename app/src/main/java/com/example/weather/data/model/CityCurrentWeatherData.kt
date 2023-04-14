package com.example.weather.data.model

class CityCurrentWeatherData(
    name: String,
    lat: Double,
    lon: Double,
    val temp: Double,
    val pressure: Int,
    val wind_speed: Double
) : CityData(name, lat, lon)