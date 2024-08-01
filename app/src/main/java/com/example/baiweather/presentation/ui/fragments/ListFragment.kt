package com.example.baiweather.presentation.ui.fragments

import android.os.Bundle
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
import com.example.baiweather.databinding.FragmentListBinding
import com.example.baiweather.presentation.adapters.CitiesAdapter
import com.example.baiweather.presentation.util.ItemDecorator
import com.example.baiweather.presentation.util.extensions.resetItemDecoration
import com.example.baiweather.presentation.viewModels.CitiesViewmodel
import com.example.baiweather.presentation.viewModels.WeatherViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val citiesAdapter by lazy {
        CitiesAdapter()
    }

    private val citiesViewmodel by viewModels<CitiesViewmodel>()
    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(com.example.baiweather.R.id.main_nav_graph)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        citiesViewmodel.getCities()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        observers()
    }

    private fun setUpRecycler() {
        binding.rvCities.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = citiesAdapter
        }

        citiesAdapter.onClick = {
            viewModel.getCurrentWeather(LatLng(it.coord.lat!!, it.coord.lon!!))
            viewModel.getDailyWeather(LatLng(it.coord.lat, it.coord.lon))
            this@ListFragment.setFragmentResult(
                getString(R.string.requestkey),
                bundleOf(getString(R.string.fromexplore) to true)
            )
            findNavController().navigateUp()
        }

        resetItemDecoration(binding.rvCities)
        binding.rvCities.addItemDecoration(ItemDecorator(12, vertical = true))
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                citiesViewmodel.searchResult.collectLatest {
                    citiesAdapter.submitList(it)
                }
            }
        }
    }
}