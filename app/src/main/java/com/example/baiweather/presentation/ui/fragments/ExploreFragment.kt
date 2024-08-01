package com.example.baiweather.presentation.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baiweather.R
import com.example.baiweather.common.Constants
import com.example.baiweather.databinding.FragmentExploreBinding
import com.example.baiweather.domain.location.LocationTracker
import com.example.baiweather.domain.util.Resource
import com.example.baiweather.presentation.adapters.CitiesAdapter
import com.example.baiweather.presentation.util.ItemDecorator
import com.example.baiweather.presentation.util.extensions.resetItemDecoration
import com.example.baiweather.presentation.viewModels.CitiesViewmodel
import com.example.baiweather.presentation.viewModels.PreferencesViewmodel
import com.example.baiweather.presentation.viewModels.WeatherViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapColorScheme
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var locationTracker: LocationTracker

    private lateinit var map: GoogleMap
    private var searchJob: Job? = null
    private val delay: Long = 500

    private val citiesAdapter by lazy {
        CitiesAdapter()
    }

    private val preferencesViewmodel: PreferencesViewmodel by viewModels()
    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(com.example.baiweather.R.id.main_nav_graph)
    private val citiesViewmodel by viewModels<CitiesViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        // Initialize the MapView
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        MapsInitializer.initialize(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        observers()
        setUpRecycler()
    }

    private fun setUpRecycler() {
        binding.rvCities.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = citiesAdapter
        }

        citiesAdapter.onClick = {
            viewModel.getWeatherByCity(LatLng(it.coord.lat!!, it.coord.lon!!))
        }
        resetItemDecoration(binding.rvCities)
        binding.rvCities.addItemDecoration(ItemDecorator(12, vertical = true))
    }


    private fun listeners() {

        binding.ivMap.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                locationTracker.getCurrentLocation()?.let { location ->
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    viewModel.getWeatherByCity(currentLocation)
                }
            }
            showMap()
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    binding.ivNoData.visibility = View.VISIBLE
                    binding.rvCities.visibility = View.GONE
                    searchJob?.cancel() // Cancel any ongoing search job when the text is empty
                } else {
                    binding.mapView.visibility = View.GONE
                    searchJob?.cancel()
                    searchJob = lifecycleScope.launch(Dispatchers.IO) {
                        delay(delay)
                        if (s.isNotEmpty()) { // Double-check if s is not empty before performing the search
                            citiesViewmodel.searchByName(s.toString())
                        }
                    }
                }
            }
        })
    }

    private fun observers() {
        preferencesViewmodel.darkMode.observe(viewLifecycleOwner) {
            if(::map.isInitialized){
                if (it == true) {
                    map.mapColorScheme = MapColorScheme.LIGHT
                } else {
                    map.mapColorScheme = MapColorScheme.DARK
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cityWeatherState.collect {
                    when (it) {
                        is Resource.Error -> {}
                        is Resource.Success -> {
                            showMap()
                            val currentLocation =
                                LatLng(it.data.coordinates?.lat!!, it.data.coordinates.lon!!)

                            try {
                                val url =
                                    URL(Constants.getIconUrl(it.data.weather?.get(0)?.icon.toString()))

                                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                                    val bmp = BitmapFactory.decodeStream(
                                        url.openConnection().getInputStream()
                                    )
                                    val smallMarker =
                                        Bitmap.createScaledBitmap(bmp, 250, 250, false)

                                    launch(Dispatchers.Main) {
                                        map.clear()
                                        map.addMarker(
                                            MarkerOptions()
                                                .position(currentLocation)
                                                .title("${it.data.name} \n Click to show weather")
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                        )

                                        map.setOnMarkerClickListener {
                                            viewModel.getCurrentWeather(it.position)
                                            viewModel.getDailyWeather(it.position)
                                            this@ExploreFragment.setFragmentResult(
                                                getString(R.string.requestkey),
                                                bundleOf(getString(R.string.fromexplore) to true)
                                            )
                                            val action =
                                                ExploreFragmentDirections.actionExploreFragmentToForecastFragment()
                                            findNavController().navigate(action)
                                            return@setOnMarkerClickListener true
                                        }

                                        map.moveCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                currentLocation,
                                                12f
                                            )
                                        )
                                    }
                                }
                            } catch (e: IOException) {
                                println(e)
                            }
                        }

                        is Resource.Loading -> {}
                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                citiesViewmodel.searchResult.collect {
                    if (it.isEmpty()) {
                        binding.ivNoData.visibility = View.VISIBLE
                        binding.rvCities.visibility = View.GONE
                    } else {
                        citiesAdapter.submitList(it)
                        binding.ivNoData.visibility = View.GONE
                        binding.rvCities.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if (preferencesViewmodel.darkMode.value == true) {
            map.mapColorScheme = MapColorScheme.LIGHT
        } else {
            map.mapColorScheme = MapColorScheme.DARK
        }

        viewLifecycleOwner.lifecycleScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                val currentLocation = LatLng(location.latitude, location.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 6f))
            }
        }

        with(map.uiSettings) {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
            isIndoorLevelPickerEnabled = true
            isMapToolbarEnabled = true
            isZoomGesturesEnabled = true
            isScrollGesturesEnabled = true
            isTiltGesturesEnabled = true
            isRotateGesturesEnabled = false
        }

        map.setOnMapClickListener {
            val currentLocation = LatLng(it.latitude, it.longitude)
            viewModel.getWeatherByCity(currentLocation)
        }
    }

    private fun showMap() {
        binding.mapView.visibility = View.VISIBLE
        binding.etSearch.clearFocus()
        binding.etSearch.text?.clear()
    }
}