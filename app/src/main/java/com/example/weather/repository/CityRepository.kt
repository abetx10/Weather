package com.example.weather.repository

import android.util.Log
import com.example.weather.data.OpenWeatherMapApi
import com.example.weather.data.model.CityData
import com.example.weather.data.model.CurrentWeatherMinimalApiResponse
import com.example.weather.data.model.FiveDayWeatherApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class CityRepository @Inject constructor() {
    private val api: OpenWeatherMapApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(OpenWeatherMapApi::class.java)
    }

    suspend fun searchCity(cityName: String, resultLimit: Int, apiKey: String): List<CityData>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchCity(cityName, resultLimit, apiKey).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCityCurrentWeatherData(
        lat: Double,
        lon: Double,
        apiKey: String
    ): CurrentWeatherMinimalApiResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCurrentWeather(lat, lon, apiKey)
                response
            } catch (e: Exception) {
                Log.e("CityRepository", "Failed to get current weather data: ${e.message}", e)
                null
            }
        }
    }

    suspend fun getCityFiveDayWeatherData(
        lat: Double,
        lon: Double,
        apiKey: String
    ): FiveDayWeatherApiResponse? {
        return withContext(Dispatchers.IO) {
            try {
                api.getCityFiveDayWeatherData(lat, lon, apiKey)
            } catch (e: Exception) {
                Log.e("CityRepository", "Failed to get 5-day weather data: ${e.message}", e)
                null
            }
        }
    }
}