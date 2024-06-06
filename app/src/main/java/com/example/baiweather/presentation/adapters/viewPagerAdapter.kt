package com.example.baiweather.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.baiweather.presentation.ui.fragments.TodayFragment
import com.example.baiweather.presentation.ui.fragments.WeekFragment

class WeatherPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            1 -> WeekFragment()
            else -> TodayFragment()
        }
    }
}