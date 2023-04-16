package com.example.weather.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.domain.model.CityFiveDayWeatherData
import com.example.weather.databinding.WeekItemBinding

class WeekAdapter(private val weekData: List<CityFiveDayWeatherData>) :
    RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    class WeekViewHolder(val binding: WeekItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val binding = WeekItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeekViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        val currentItem = weekData[position]
        with(holder.binding) {
            tvDate.text = currentItem.date
            tvTempMinValue.text = currentItem.tempMin.toString()
            tvTempMaxValue.text = currentItem.tempMax.toString()
        }
    }

    override fun getItemCount(): Int = weekData.size
}