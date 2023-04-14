package com.example.weather.data.model

import com.google.gson.annotations.SerializedName

data class FiveDayWeatherApiResponse(
    val list: List<WeatherEntry>
)

data class WeatherEntry(
    @SerializedName("dt_txt") val dt_txt: String,
    val main: MainData
)

data class MainData(
    @SerializedName("temp_min") val temp_min: Double,
    @SerializedName("temp_max") val temp_max: Double
)