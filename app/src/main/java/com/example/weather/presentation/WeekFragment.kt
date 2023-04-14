package com.example.weather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.FragmentWeekBinding
import com.example.weather.presentation.adapters.WeekAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeekFragment : Fragment() {

    private lateinit var binding: FragmentWeekBinding
    lateinit var viewModel: ViewModelMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeekBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        binding.weekRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            viewModel.cityFiveDayWeatherDataMap.observe(viewLifecycleOwner) { cityWeatherDataMap ->
                cityWeatherDataMap[city.name]?.let { weatherDataList ->
                    binding.weekRecyclerView.adapter = WeekAdapter(weatherDataList)
                } ?: run {
                    binding.weekRecyclerView.adapter = WeekAdapter(emptyList())
                }
            }
        }
    }
}