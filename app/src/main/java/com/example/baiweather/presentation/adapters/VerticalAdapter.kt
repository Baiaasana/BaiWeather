package com.example.baiweather.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baiweather.R
import com.example.baiweather.databinding.VerticalItemBinding
import com.example.baiweather.domain.model.ForecastData
import com.example.baiweather.presentation.util.extensions.setImage

class VerticalAdapter :
    ListAdapter<ForecastData, VerticalAdapter.VerticalViewHolder>(DiffCallback) {

    lateinit var context: Context

    inner class VerticalViewHolder(private var binding: VerticalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastData) = with(binding) {
            ivWeather.setImage(item.icon)
            tvDay.text = item.time
            tvTemperature.text =
                context.resources.getString(R.string.temperatures, item.temp, item.maxTemp)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalViewHolder {
        context = parent.context
        return VerticalViewHolder(
            VerticalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VerticalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        val DiffCallback = object : DiffUtil.ItemCallback<ForecastData>() {
            override fun areItemsTheSame(
                oldItem: ForecastData,
                newItem: ForecastData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ForecastData,
                newItem: ForecastData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}