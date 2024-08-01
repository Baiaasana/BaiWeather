package com.example.baiweather.presentation.ui.fragments

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.baiweather.R
import com.example.baiweather.common.Constants
import com.example.baiweather.data.remote.model.CurrentWeatherDto
import com.example.baiweather.databinding.FragmentForecastBinding
import com.example.baiweather.domain.util.Resource
import com.example.baiweather.presentation.adapters.WeatherPagerAdapter
import com.example.baiweather.presentation.util.ConnectivityLiveData
import com.example.baiweather.presentation.util.extensions.setImage
import com.example.baiweather.presentation.viewModels.PreferencesViewmodel
import com.example.baiweather.presentation.viewModels.WeatherViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!

    private lateinit var connectivityLiveData: ConnectivityLiveData

    private var froSearch = false

    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(com.example.baiweather.R.id.main_nav_graph)
    private val preferencesViewModel: PreferencesViewmodel by activityViewModels()

    private lateinit var weatherPagerAdapter: WeatherPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(getString(R.string.requestkey)) { requestKey, bundle ->
            val result = bundle.getBoolean(getString(R.string.fromexplore))
            froSearch = result
        }
        connectivityLiveData = ConnectivityLiveData(application = requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        viewLifecycleOwner.lifecycleScope.launch {
            preferencesViewModel.darkMode.observe(viewLifecycleOwner) {
                if (it) {
                    binding.ivDarkMode.setImageResource(com.example.baiweather.R.drawable.sun)
                } else {
                    binding.ivDarkMode.setImageResource(com.example.baiweather.R.drawable.moon)
                }
            }
        }

        if(froSearch){
            froSearch = false
            viewModel.onFragmentReady()
        }
        checkConnection()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listeners()
    }

    private fun checkConnection() {
        connectivityLiveData.observe(viewLifecycleOwner) { isAvailable ->
            when (isAvailable) {
                true -> {
                    observers()
                    showView()
                }
                false -> {
                    hideView()
                }
            }
        }
    }

    private fun showView() {
        binding.tvNoInternet.visibility = View.GONE
        binding.clWeather.visibility = View.VISIBLE
        binding.tabLayout.visibility = View.VISIBLE
        binding.pager.visibility = View.VISIBLE
    }

    private fun hideView() {
        binding.tvNoInternet.visibility = View.VISIBLE
        binding.clWeather.visibility = View.GONE
        binding.tabLayout.visibility = View.GONE
        binding.pager.visibility = View.GONE
    }

    private fun init() {
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
                    preferencesViewModel.saveDarkMode(false)
                } else {
                    preferencesViewModel.saveDarkMode(true)
                }
            }
        }

        binding.ivBookmark.setOnClickListener {
            binding.ivBookmark.isSelected = !binding.ivBookmark.isSelected
        }
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentWeatherState.collect {
                    when (it) {
                        is Resource.Error -> {
                            if (it.message!!.isNotBlank()) {
                                binding.tvError.text = it.toString()
                                binding.tvError.visibility = View.VISIBLE
                            } else {
                                binding.tvError.visibility = View.GONE
                            }
                            binding.progressBar.visibility = View.GONE
                            binding.progressBar2.visibility = View.GONE
                        }

                        is Resource.Success -> {
                            setUpUI(it.data)
                            binding.progressBar.visibility = View.GONE
                            binding.progressBar2.visibility = View.GONE
                        }

                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.progressBar2.visibility = View.VISIBLE
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setUpUI(data: CurrentWeatherDto) = with(binding) {
        tvLocation.text = data.name.toString()
        tvTemperature.text = resources.getString(
            com.example.baiweather.R.string.temperature, data.main?.temp?.roundToInt()
        )
        tvLowTemperature.text = resources.getString(
            com.example.baiweather.R.string.temperature, data.main?.tempMin?.roundToInt()
        )
        tvHighTemperature.text = resources.getString(
            com.example.baiweather.R.string.temperature, data.main?.tempMax?.roundToInt()
        )
        tvFeelsLike.text = resources.getString(
            com.example.baiweather.R.string.temperature, data.main?.feelsLike?.roundToInt()
        )
        tvDescription.text = data.weather?.get(0)?.description.toString()
        ivWeather.setImage(Constants.getIconUrl(data.weather?.get(0)?.icon.toString()))
    }

}