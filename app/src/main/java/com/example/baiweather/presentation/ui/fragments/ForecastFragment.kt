package com.example.baiweather.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.example.baiweather.R
import com.example.baiweather.common.Constants
import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.databinding.FragmentForecastBinding
import com.example.baiweather.presentation.WeatherViewModel
import com.example.baiweather.presentation.adapters.WeatherPagerAdapter
import com.example.baiweather.presentation.util.extensions.setImage
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(R.id.main_nav_graph)
    private lateinit var weatherPagerAdapter: WeatherPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listeners()
        observers()

    }

    private fun init() {
        viewModel.getCurrentWeather()
        viewModel.getDailyWeather()
        weatherPagerAdapter = WeatherPagerAdapter(this@ForecastFragment)
        binding.pager.adapter = weatherPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.today)
                }

                1 -> {
                    tab.text = getString(R.string.forecast)
                }
            }
        }.attach()

    }

    private fun listeners() {
        binding.pager.isUserInputEnabled = false

    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentWeatherState.collectLatest {
                it.data?.let { currentData ->
                    setUpUI(currentData)
                }
            }
        }
    }

    private fun setUpUI(data: CurrentWeatherDto) = with(binding) {
        tvLocation.text = data.name.toString()
        tvTemperature.text =
            resources.getString(R.string.temperature, data.main?.temp?.roundToInt())
        tvLowTemperature.text =
            resources.getString(R.string.temperature, data.main?.tempMin?.roundToInt())
        tvHighTemperature.text =
            resources.getString(R.string.temperature, data.main?.tempMax?.roundToInt())
        tvFeelsLike.text =
            resources.getString(R.string.temperature, data.main?.feelsLike?.roundToInt())
        tvDescription.text = resources.getString(
            R.string.weather_desc,
            data.weather?.get(0)?.main.toString(),
            data.weather?.get(0)?.description.toString()
        )
        ivWeather.setImage(Constants.getIconUrl(data.weather?.get(0)?.icon.toString()))
    }
}