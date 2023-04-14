package com.example.weather.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.R
import com.example.weather.WeatherApplication
import com.example.weather.data.WeatherDataManager
import com.example.weather.data.model.CityCurrentWeatherData
import com.example.weather.data.model.CityData
import com.example.weather.data.model.CityFiveDayWeatherData
import com.example.weather.data.processWeatherData
import com.example.weather.repository.CityRepository
import kotlinx.coroutines.*
import kotlin.math.min
import kotlin.math.max


class ViewModelMainActivity(private val application: WeatherApplication) : ViewModel() {
    private val cityRepository = CityRepository()
    private val weatherDataManager = WeatherDataManager(cityRepository)
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _cityDataList = MutableLiveData<List<CityData>>()
    val cityDataList: LiveData<List<CityData>> = _cityDataList

    private val _cityCurrentWeatherDataList = MutableLiveData<List<CityCurrentWeatherData>>()
    val cityCurrentWeatherDataList: LiveData<List<CityCurrentWeatherData>> = _cityCurrentWeatherDataList

    private val _selectedCity = MutableLiveData<CityCurrentWeatherData>()
    val selectedCity: LiveData<CityCurrentWeatherData> = _selectedCity

    private val _cityFiveDayWeatherDataMap = MutableLiveData<Map<String, List<CityFiveDayWeatherData>>>()
    val cityFiveDayWeatherDataMap: LiveData<Map<String, List<CityFiveDayWeatherData>>> = _cityFiveDayWeatherDataMap

    init {
        loadData()
    }

    private fun loadData() {
        coroutineScope.launch {
            val cityDataList = loadCitiesData()
            loadCityCurrentWeatherData(cityDataList)
            loadCityFiveDayWeatherData(cityDataList)
        }
    }

    private suspend fun loadCitiesData(): List<CityData> {
        val cities = application.resources.getStringArray(R.array.cities)
        val apiKey = application.openWeatherMapApiKey

        val cityDataList = withContext(Dispatchers.IO) {
            weatherDataManager.loadCitiesData(cities, apiKey)
        }
        _cityDataList.value = cityDataList

        cityDataList.forEach { cityData ->
            Log.d("MainActivityViewModel", "City: ${cityData.name}, Latitude: ${cityData.lat}, Longitude: ${cityData.lon}")
        }

        return cityDataList
    }

    private suspend fun loadCityCurrentWeatherData(cityDataList: List<CityData>) {
        val apiKey = application.openWeatherMapApiKey

        val cityCurrentWeatherDataList = withContext(Dispatchers.IO) {
            weatherDataManager.loadCityCurrentWeatherData(cityDataList, apiKey)
        }
        _cityCurrentWeatherDataList.value = cityCurrentWeatherDataList

        cityCurrentWeatherDataList.forEach { cityCurrentWeatherData ->
            Log.d("MainActivityViewModel", "City: ${cityCurrentWeatherData.name}, Latitude: ${cityCurrentWeatherData.lat}, Longitude: ${cityCurrentWeatherData.lon}, Temperature: ${cityCurrentWeatherData.temp}, Pressure: ${cityCurrentWeatherData.pressure}, Wind speed: ${cityCurrentWeatherData.wind_speed}")
        }
    }

    private fun loadCityFiveDayWeatherData(cityDataList: List<CityData>) {
        val apiKey = application.openWeatherMapApiKey

        coroutineScope.launch {
            Log.d("MainActivityViewModel", "Loading five-day weather data for cities")
            val cityFiveDayWeatherDataMap = mutableMapOf<String, List<CityFiveDayWeatherData>>()
            for (cityData in cityDataList) {
                val cityFiveDayWeatherDataList = weatherDataManager.loadCityFiveDayWeatherData(cityData, apiKey)
                val processedCityWeatherDataList = cityFiveDayWeatherDataList.processWeatherData()
                cityFiveDayWeatherDataMap[cityData.name] = processedCityWeatherDataList
            }
            _cityFiveDayWeatherDataMap.value = cityFiveDayWeatherDataMap

            cityFiveDayWeatherDataMap.forEach { (cityName, cityWeatherDataList) ->
                Log.d("MainActivityViewModel", "City: $cityName")
                cityWeatherDataList.forEach { cityWeatherData ->
                    Log.d("MainActivityViewModel", "Date: ${cityWeatherData.date}, Min temperature: ${cityWeatherData.tempMin}, Max temperature: ${cityWeatherData.tempMax}")
                }
            }
        }
    }

    fun setSelectedCity(cityName: String) {
        _cityCurrentWeatherDataList.value?.let { cityDailyWeatherDataList ->
            val city = cityDailyWeatherDataList.find { it.name == cityName }
            if (city != null) {
                _selectedCity.value = city!!
            } else {
                Log.e("ViewModelMainActivity", "City not found: $cityName")
            }
        } ?: run {
            Log.e("ViewModelMainActivity", "CityDailyWeatherDataList is null")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}