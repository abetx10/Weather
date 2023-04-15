package com.example.weather.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.data.WeatherDataManager
import com.example.weather.data.model.CityCurrentWeatherData
import com.example.weather.data.model.CityData
import com.example.weather.data.model.CityFiveDayWeatherData
import com.example.weather.data.processWeatherData
import com.example.weather.data.room.*
import com.example.weather.utils.isConnectedToInternet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ViewModelMainActivity @Inject constructor(
    application: Application,
    private val weatherDataManager: WeatherDataManager,
    private val apiKey: String,
    private val cities: Array<String>,
    private val weatherDao: WeatherDao
) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _cityDataList = MutableLiveData<List<CityData>>()
    val cityDataList: LiveData<List<CityData>> = _cityDataList

    private val _cityCurrentWeatherDataList = MutableLiveData<List<CityCurrentWeatherData>>()
    val cityCurrentWeatherDataList: LiveData<List<CityCurrentWeatherData>> =
        _cityCurrentWeatherDataList

    private val _cityFiveDayWeatherDataMap = MutableLiveData<Map<String, List<CityFiveDayWeatherData>>>()
    val cityFiveDayWeatherDataMap: LiveData<Map<String, List<CityFiveDayWeatherData>>> = _cityFiveDayWeatherDataMap

    private val _selectedCity = MutableLiveData<CityCurrentWeatherData>()
    val selectedCity: LiveData<CityCurrentWeatherData> = _selectedCity

    init {
        loadData()
    }

    private fun loadData() {
        coroutineScope.launch {
            val cityDataList = loadCitiesData()

            if (isConnectedToInternet(getApplication())) {
                loadCityCurrentWeatherData(cityDataList)
                loadCityFiveDayWeatherData(cityDataList)
            } else {
                loadCurrentWeatherDataFromRoom()
                loadFiveDayWeatherDataFromRoom()
            }
        }
    }

    private suspend fun loadCitiesData(): List<CityData> {
        val cityDataList = withContext(Dispatchers.IO) {
            weatherDataManager.loadCitiesData(cities, apiKey)
        }
        _cityDataList.value = cityDataList

        cityDataList.forEach { cityData ->
            Log.d(
                "MainActivityViewModel",
                "City: ${cityData.name}, Latitude: ${cityData.lat}, Longitude: ${cityData.lon}"
            )
        }

        return cityDataList
    }

    private suspend fun loadCityCurrentWeatherData(cityDataList: List<CityData>) {
        val cityCurrentWeatherDataList = withContext(Dispatchers.IO) {
            weatherDataManager.loadCityCurrentWeatherData(cityDataList, apiKey)
        }
        _cityCurrentWeatherDataList.value = cityCurrentWeatherDataList
        saveCurrentWeatherDataToRoom(cityCurrentWeatherDataList)

        cityCurrentWeatherDataList.forEach { cityCurrentWeatherData ->
            Log.d(
                "MainActivityViewModel",
                "City: ${cityCurrentWeatherData.name}, Latitude: ${cityCurrentWeatherData.lat}, Longitude: ${cityCurrentWeatherData.lon}, Temperature: ${cityCurrentWeatherData.temp}, Pressure: ${cityCurrentWeatherData.pressure}, Wind speed: ${cityCurrentWeatherData.wind_speed}"
            )
        }
    }

    private fun loadCityFiveDayWeatherData(cityDataList: List<CityData>) {
        coroutineScope.launch {
            Log.d("MainActivityViewModel", "Loading five-day weather data for cities")
            val cityFiveDayWeatherDataMap = mutableMapOf<String, List<CityFiveDayWeatherData>>()
            for (cityData in cityDataList) {
                val cityFiveDayWeatherDataList =
                    weatherDataManager.loadCityFiveDayWeatherData(cityData, apiKey)
                val processedCityWeatherDataList = cityFiveDayWeatherDataList.processWeatherData()
                cityFiveDayWeatherDataMap[cityData.name] = processedCityWeatherDataList
            }
            _cityFiveDayWeatherDataMap.value = cityFiveDayWeatherDataMap
            saveFiveDayWeatherDataToRoom(cityFiveDayWeatherDataMap)


            cityFiveDayWeatherDataMap.forEach { (cityName, cityWeatherDataList) ->
                Log.d("MainActivityViewModel", "City: $cityName")
                cityWeatherDataList.forEach { cityWeatherData ->
                    Log.d(
                        "MainActivityViewModel",
                        "Date: ${cityWeatherData.date}, Min temperature: ${cityWeatherData.tempMin}, Max temperature: ${cityWeatherData.tempMax}"
                    )
                }
            }
        }
    }

    private suspend fun saveCurrentWeatherDataToRoom(cityCurrentWeatherDataList: List<CityCurrentWeatherData>) {
        withContext(Dispatchers.IO) {
            cityCurrentWeatherDataList.forEach { cityCurrentWeatherData ->
                val entity = cityCurrentWeatherData.toCurrentWeatherEntity()
                weatherDao.insertCurrentWeather(entity)
                Log.d("ViewModelMainActivity", "Saved current weather data for city: ${cityCurrentWeatherData.name}")
            }
        }
    }

    private suspend fun saveFiveDayWeatherDataToRoom(cityFiveDayWeatherDataMap: Map<String, List<CityFiveDayWeatherData>>) {
        withContext(Dispatchers.IO) {
            cityFiveDayWeatherDataMap.forEach { (cityName, cityWeatherDataList) ->
                cityWeatherDataList.forEach { cityFiveDayWeatherData ->
                    val entity = cityFiveDayWeatherData.toFiveDayWeatherEntity()
                    weatherDao.insertFiveDayWeather(entity)
                    Log.d("ViewModelMainActivity", "Saved five-day weather data for city: $cityName, date: ${cityFiveDayWeatherData.date}")
                }
            }
        }
    }

    private suspend fun loadCurrentWeatherDataFromRoom() {
        withContext(Dispatchers.IO) {
            val currentWeatherEntityList = weatherDao.getAllCurrentWeather()
            val cityCurrentWeatherDataList = currentWeatherEntityList.map { it.toCityCurrentWeatherData() }

            cityCurrentWeatherDataList.forEach { cityCurrentWeatherData ->
                Log.d("ViewModelMainActivity", "Loaded CurrentWeather from Room - City: ${cityCurrentWeatherData.name}, Temperature: ${cityCurrentWeatherData.temp}, Pressure: ${cityCurrentWeatherData.pressure}, Wind speed: ${cityCurrentWeatherData.wind_speed}")
            }

            withContext(Dispatchers.Main) {
                _cityCurrentWeatherDataList.value = cityCurrentWeatherDataList
            }
        }
    }

    private suspend fun loadFiveDayWeatherDataFromRoom() {
        withContext(Dispatchers.IO) {
            val cityNames = cities.toList()
            val cityFiveDayWeatherDataMap = mutableMapOf<String, List<CityFiveDayWeatherData>>()
            for (cityName in cityNames) {
                val fiveDayWeatherEntityList = weatherDao.getFiveDayWeather(cityName)
                val cityFiveDayWeatherDataList = fiveDayWeatherEntityList.map { it.toCityFiveDayWeatherData() }

                cityFiveDayWeatherDataList.forEach { cityFiveDayWeatherData ->
                    Log.d("ViewModelMainActivity", "Loaded FiveDayWeather from Room - City: $cityName, Date: ${cityFiveDayWeatherData.date}, Min temperature: ${cityFiveDayWeatherData.tempMin}, Max temperature: ${cityFiveDayWeatherData.tempMax}")
                }

                cityFiveDayWeatherDataMap[cityName] = cityFiveDayWeatherDataList
            }
            withContext(Dispatchers.Main) {
                _cityFiveDayWeatherDataMap.value = cityFiveDayWeatherDataMap
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