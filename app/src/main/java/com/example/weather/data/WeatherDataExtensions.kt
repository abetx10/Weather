package com.example.weather.data

import kotlin.math.min
import kotlin.math.max
import com.example.weather.data.model.CityFiveDayWeatherData

    fun List<CityFiveDayWeatherData>.processWeatherData(): List<CityFiveDayWeatherData> {
        val dailyTemperatureData = mutableMapOf<String, Pair<Int, Int>>()

        for (weatherData in this) {
            val currentDate = weatherData.date.substring(0, 10)

            if (!dailyTemperatureData.containsKey(currentDate)) {
                dailyTemperatureData[currentDate] = Pair(weatherData.tempMin, weatherData.tempMax)
            } else {
                val currentMinMax = dailyTemperatureData[currentDate]!!
                val newMin = min(currentMinMax.first, weatherData.tempMin)
                val newMax = max(currentMinMax.second, weatherData.tempMax)
                dailyTemperatureData[currentDate] = Pair(newMin, newMax)
            }
        }

        val resultList = mutableListOf<CityFiveDayWeatherData>()

        for ((date, minMax) in dailyTemperatureData) {
            val cityWeatherData = CityFiveDayWeatherData(
                date = date,
                tempMin = minMax.first,
                tempMax = minMax.second,
                name = this[0].name,
                lat = this[0].lat,
                lon = this[0].lon
            )
            resultList.add(cityWeatherData)
        }

        return resultList
    }