package com.example.weather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.presentation.adapters.WeatherData
import com.example.weather.presentation.adapters.WeekAdapter


class WeekFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)
        recyclerView = view.findViewById(R.id.week_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Создаем тестовые данные
        val testData = listOf(
            WeatherData("2023-04-12", 10, 20),
            WeatherData("2023-04-13", 11, 19),
            WeatherData("2023-04-14", 9, 18)
        )

        // Устанавливаем адаптер с тестовыми данными
        recyclerView.adapter = WeekAdapter(testData)

        return view
    }
}
