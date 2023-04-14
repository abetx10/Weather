package com.example.weather.presentation.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.weather.R

class CitySelectionDialog(context: Context, private val onCitySelected: (String) -> Unit) : AlertDialog.Builder(context) {

    init {
        val cities = context.resources.getStringArray(R.array.cities)

        setTitle("Please select city")
        setItems(cities) { _, which ->
            // Здесь обрабатывается выбор города
            val selectedCity = cities[which]
            onCitySelected(selectedCity)
        }
        setNegativeButton(context.getString(R.string.cancel), null)
    }
}