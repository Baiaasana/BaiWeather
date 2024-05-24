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
import com.example.baiweather.databinding.FragmentTodayBinding
import com.example.baiweather.presentation.WeatherViewModel
import com.example.baiweather.presentation.adapters.ListItem
import com.example.baiweather.presentation.adapters.SuperAdapter
import com.example.baiweather.presentation.mappers.DailyWeatherDto.toGridData
import com.example.baiweather.presentation.mappers.DailyWeatherDto.toHourlyData
import com.example.baiweather.presentation.util.ItemDecorator
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(R.id.main_nav_graph)

    private val superAdapter by lazy { SuperAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.tag("dhfgdhjfgd").d("fragment onCreateView")
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        observers()
    }

    private fun setUpRecycler() {

        binding.rvSuper.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = superAdapter
        }

        if (binding.rvSuper.itemDecorationCount != 0) {
            binding.rvSuper.removeItemDecorationAt(0)
        }
        binding.rvSuper.addItemDecoration(ItemDecorator(12, vertical = true))

    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dailyWeatherState
                .filter { it.data != null } // Filter out states with null data
                .collect { dailyData ->
                    val items = listOf(
                        dailyData.data?.toHourlyData()
                            ?.let { ListItem.Horizontal(0, "Horizontal Items", it) },
                        dailyData.data?.toGridData()
                            ?.let { ListItem.Grid(1, "Grid Items", it) },
                    )
                    Timber.tag("collect").d("submit")
                    superAdapter.submitList(items)
                }
        }
    }
}
