package com.example.baiweather.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baiweather.R
import com.example.baiweather.databinding.FragmentWeekBinding
import com.example.baiweather.presentation.WeatherViewModel
import com.example.baiweather.presentation.adapters.ForecastAdapter
import com.example.baiweather.presentation.mappers.DailyWeatherDto.toForecastData
import com.example.baiweather.presentation.util.ItemDecorator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WeekFragment : Fragment() {
    private var _binding: FragmentWeekBinding? = null
    private val binding get() = _binding!!

    private val forecastAdapter by lazy { ForecastAdapter() }
    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(R.id.main_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        observers()
    }

    private fun setUpRecycler() {
        binding.rvForecast.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = forecastAdapter
        }
        if(binding.rvForecast.itemDecorationCount != 0){
            binding.rvForecast.removeItemDecorationAt(0)
        }
        binding.rvForecast.addItemDecoration(ItemDecorator(12, vertical = true))

    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dailyWeatherState.collectLatest {
                it.data?.let { dailyData ->
                    forecastAdapter.submitList(dailyData.toForecastData())
                }
            }
        }
    }
}