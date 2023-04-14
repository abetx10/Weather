package com.example.weather.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {

    private lateinit var binding: FragmentTodayBinding
    lateinit var viewModel: ViewModelMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            binding.tvTemperatureValue.text = city.temp.toString()
            binding.tvPressureValue.text = city.pressure.toString()
            binding.tvWindSpeedValue.text = city.wind_speed.toString()
        }
    }
}
