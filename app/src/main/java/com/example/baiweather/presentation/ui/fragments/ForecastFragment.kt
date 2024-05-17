package com.example.baiweather.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.example.baiweather.R
import com.example.baiweather.databinding.FragmentForecastBinding
import com.example.baiweather.presentation.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(R.id.main_nav_graph)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        observers()
    }

    private fun listeners() {
        binding.btnCurrent.setOnClickListener {
            viewModel.getCurrentWeather()
        }
        binding.btnDaily.setOnClickListener {
            viewModel.getDailyWeather()
        }


    }

    private fun observers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentWeatherState.collect(){
                Timber.tag("currentWeatherState").d(it.toString()).toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dailyWeatherState.collect(){
                Timber.tag("dailyWeatherState").d(it.toString()).toString()
            }
        }
    }
}