package com.example.baiweather.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baiweather.R
import com.example.baiweather.databinding.ForecastItemBinding
import com.example.baiweather.databinding.HourlyItemBinding
import com.example.baiweather.domain.data.ForecastData
import com.example.baiweather.presentation.util.extensions.setImage

class ForecastAdapter : ListAdapter<ForecastData, RecyclerView.ViewHolder>(DiffCallback) {

    lateinit var context: Context

    inner class HourlyViewHolder(private var binding: HourlyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastData) = with(binding) {
            ivWeather.setImage(item.icon)
            tvTemperature.text = context.resources.getString(R.string.temperature, item.temp)
            tvTime.text = item.time
            binding.root.context
        }
    }

    inner class ForecastViewHolder(private var binding: ForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastData) = with(binding) {
            ivWeather.setImage(item.icon)
            tvDay.text = item.time
            tvTemperature.text =
                context.resources.getString(R.string.temperatures, item.temp, item.maxTemp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            ITEM_HOURLY -> HourlyViewHolder(
                HourlyItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ITEM_FORECAST -> ForecastViewHolder(
                ForecastItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HourlyViewHolder -> {
                val item = getItem(position) as ForecastData
                holder.bind(item)
            }

            is ForecastViewHolder -> {
                val item = getItem(position) as ForecastData
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isHourly) ITEM_HOURLY else ITEM_FORECAST
    }

    companion object {
        private const val ITEM_HOURLY = 0
        private const val ITEM_FORECAST = 1

        val DiffCallback = object : DiffUtil.ItemCallback<ForecastData>() {
            override fun areItemsTheSame(oldItem: ForecastData, newItem: ForecastData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ForecastData, newItem: ForecastData): Boolean {
                return oldItem.time == newItem.time
            }
        }
    }
}