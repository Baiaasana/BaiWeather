package com.example.baiweather.presentation.ui.fragments

import android.R
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.baiweather.common.Constants
import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.databinding.FragmentForecastBinding
import com.example.baiweather.presentation.DataStoreViewModel
import com.example.baiweather.presentation.WeatherViewModel
import com.example.baiweather.presentation.adapters.WeatherPagerAdapter
import com.example.baiweather.presentation.util.extensions.setImage
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt


@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(com.example.baiweather.R.id.main_nav_graph)

//    private val darkModePreferences by lazy { DarkModePreferences(requireContext()) }


    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    private lateinit var weatherPagerAdapter: WeatherPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        dataStoreViewModel.darkMode.observe(viewLifecycleOwner, Observer { isLightMode ->
            if (isLightMode) {
                binding.ivDarkMode.setImageResource(com.example.baiweather.R.drawable.sun)
            } else {
                binding.ivDarkMode.setImageResource(com.example.baiweather.R.drawable.moon)
            }
        })
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
                    tab.text = getString(com.example.baiweather.R.string.today)
                }

                1 -> {
                    tab.text = getString(com.example.baiweather.R.string.forecast)
                }
            }
        }.attach()

    }

    private fun listeners() {
        binding.pager.isUserInputEnabled = false
        binding.ivDarkMode.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val currentNightMode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK
                if (currentNightMode == UI_MODE_NIGHT_NO) {
                    dataStoreViewModel.saveDarkMode(false)
                } else {
                    dataStoreViewModel.saveDarkMode(true)
                }
            }
//            requireActivity().recreate()
        }
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
            resources.getString(com.example.baiweather.R.string.temperature, data.main?.temp?.roundToInt())
        tvLowTemperature.text =
            resources.getString(com.example.baiweather.R.string.temperature, data.main?.tempMin?.roundToInt())
        tvHighTemperature.text =
            resources.getString(com.example.baiweather.R.string.temperature, data.main?.tempMax?.roundToInt())
        tvFeelsLike.text =
            resources.getString(com.example.baiweather.R.string.temperature, data.main?.feelsLike?.roundToInt())
        tvDescription.text = resources.getString(
            com.example.baiweather.R.string.weather_desc,
            data.weather?.get(0)?.main.toString(),
            data.weather?.get(0)?.description.toString()
        )
        ivWeather.setImage(Constants.getIconUrl(data.weather?.get(0)?.icon.toString()))
    }
}