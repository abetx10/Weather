package com.example.weather.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey
    val name: String,
    val lat: Double,
    val lon: Double,
    val temp: Double,
    val pressure: Int,
    val wind_speed: Double
)

@Entity(tableName = "five_day_weather")
data class FiveDayWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityName: String,
    val lat: Double,
    val lon: Double,
    val date: String,
    val tempMin: Int,
    val tempMax: Int
)