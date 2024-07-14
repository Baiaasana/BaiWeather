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
import com.example.baiweather.domain.util.Resource
import com.example.baiweather.presentation.adapters.ListItem
import com.example.baiweather.presentation.adapters.SuperAdapter
import com.example.baiweather.presentation.mappers.toGridData
import com.example.baiweather.presentation.mappers.toHourlyData
import com.example.baiweather.presentation.viewModels.WeatherViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(R.id.main_nav_graph)

    private val superAdapter by lazy { SuperAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.dailyWeatherState.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        val items = listOf(
                            it.data.toHourlyData()
                                .let { ListItem.Horizontal(0, getString(R.string.hourly), it) },
                            it.data.toGridData()
                                .let { ListItem.Grid(1, getString(R.string.forecast), it) },
                        )
                        superAdapter.submitList(items)
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
