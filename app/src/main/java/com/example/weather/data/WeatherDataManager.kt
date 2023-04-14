package com.example.weather.data

import android.util.Log
import com.example.weather.data.model.CityCurrentWeatherData
import com.example.weather.data.model.CityData
import com.example.weather.data.model.CityFiveDayWeatherData
import com.example.weather.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherDataManager @Inject constructor(private val cityRepository: CityRepository) {

    suspend fun loadCitiesData(cities: Array<String>, apiKey: String): List<CityData> {
        val cityDataList = mutableListOf<CityData>()

        for (city in cities) {
            val cityData = searchCity(city, 1, apiKey)
            if (cityData != null) {
                val roundedLat = String.format("%.2f", cityData.lat).toDouble()
                val roundedLon = String.format("%.2f", cityData.lon).toDouble()
                val roundedCityData = CityData(cityData.name, roundedLat, roundedLon)
                cityDataList.add(roundedCityData)
            }
        }
        return cityDataList
    }

    private suspend fun searchCity(cityName: String, resultLimit: Int, apiKey: String): CityData? {
        return withContext(Dispatchers.IO) {
            val cityDataList = cityRepository.searchCity(cityName, resultLimit, apiKey)
            if (cityDataList != null && cityDataList.isNotEmpty()) {
                cityDataList[0]
            } else {
                null
            }
        }
    }

    suspend fun loadCityCurrentWeatherData(
        cityDataList: List<CityData>,
        apiKey: String
    ): List<CityCurrentWeatherData> {
        val cityCurrentWeatherDataList = mutableListOf<CityCurrentWeatherData>()

        for (cityData in cityDataList) {
            val currentWeatherApiResponse =
                cityRepository.getCityCurrentWeatherData(cityData.lat, cityData.lon, apiKey)
            if (currentWeatherApiResponse != null) {
                val cityCurrentWeatherData = CityCurrentWeatherData(
                    cityData.name,
                    cityData.lat,
                    cityData.lon,
                    currentWeatherApiResponse.main.temp,
                    currentWeatherApiResponse.main.pressure,
                    currentWeatherApiResponse.wind.speed
                )
                cityCurrentWeatherDataList.add(cityCurrentWeatherData)
            } else {
                Log.e(
                    "WeatherDataManager",
                    "Failed to load current weather data for city: ${cityData.name}"
                )
            }
        }
        return cityCurrentWeatherDataList
    }

    suspend fun loadCityFiveDayWeatherData(
        cityData: CityData,
        apiKey: String
    ): List<CityFiveDayWeatherData> {
        val weatherDataList = mutableListOf<CityFiveDayWeatherData>()

        val fiveDayWeatherApiResponse =
            cityRepository.getCityFiveDayWeatherData(cityData.lat, cityData.lon, apiKey)

        if (fiveDayWeatherApiResponse != null) {
            for (weatherEntry in fiveDayWeatherApiResponse.list) {
                val date = weatherEntry.dt_txt
                val tempMin = weatherEntry.main.temp_min.toInt()
                val tempMax = weatherEntry.main.temp_max.toInt()

                val cityFiveDayWeatherData = CityFiveDayWeatherData(
                    date,
                    tempMin,
                    tempMax,
                    cityData.name,
                    cityData.lat,
                    cityData.lon
                )
                weatherDataList.add(cityFiveDayWeatherData)
                Log.d(
                    "WeatherDataManager",
                    "City: ${cityData.name}, Date: $date, TempMin: $tempMin, TempMax: $tempMax"
                )
            }
        } else {
            Log.e(
                "WeatherDataManager",
                "Failed to load 5-day weather data for city: ${cityData.name}"
            )
        }

        return weatherDataList
    }
}