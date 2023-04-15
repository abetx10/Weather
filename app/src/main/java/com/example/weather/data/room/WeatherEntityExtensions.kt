package com.example.weather.data.room

import com.example.weather.data.model.CityCurrentWeatherData
import com.example.weather.data.model.CityFiveDayWeatherData

fun CityCurrentWeatherData.toCurrentWeatherEntity(): CurrentWeatherEntity {
    return CurrentWeatherEntity(
            name = this.name,
            lat = this.lat,
            lon = this.lon,
            temp = this.temp,
            pressure = this.pressure,
            wind_speed = this.wind_speed
    )
}

fun CurrentWeatherEntity.toCityCurrentWeatherData(): CityCurrentWeatherData {
        return CityCurrentWeatherData(
            name = this.name,
            lat = this.lat,
            lon = this.lon,
            temp = this.temp,
            pressure = this.pressure,
            wind_speed = this.wind_speed
    )
}

fun CityFiveDayWeatherData.toFiveDayWeatherEntity(): FiveDayWeatherEntity {
    return FiveDayWeatherEntity(
        cityName = this.name,
        lat = this.lat,
        lon = this.lon,
        date = this.date,
        tempMin = this.tempMin,
        tempMax = this.tempMax
    )
}

fun FiveDayWeatherEntity.toCityFiveDayWeatherData(): CityFiveDayWeatherData {
    return CityFiveDayWeatherData(
        name = this.cityName,
        lat = this.lat,
        lon = this.lon,
        date = this.date,
        tempMin = this.tempMin,
        tempMax = this.tempMax
    )
}