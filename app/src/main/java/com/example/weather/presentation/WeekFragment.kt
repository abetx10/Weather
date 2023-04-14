package com.example.weather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.presentation.adapters.WeekAdapter


class WeekFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ViewModelMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week, container, false)
        recyclerView = view.findViewById(R.id.week_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel = (activity as MainActivity).viewModel

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            viewModel.cityFiveDayWeatherDataMap.observe(viewLifecycleOwner) { cityWeatherDataMap ->
                cityWeatherDataMap[city.name]?.let { weatherDataList ->
                    recyclerView.adapter = WeekAdapter(weatherDataList)
                } ?: run {
                    recyclerView.adapter = WeekAdapter(emptyList())
                }
            }
        }
    }
}
