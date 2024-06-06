package com.example.baiweather.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baiweather.R
import com.example.baiweather.databinding.HorizontalItemBinding
import com.example.baiweather.domain.model.ForecastData
import com.example.baiweather.presentation.util.extensions.setImage
import timber.log.Timber

class HorizontalAdapter :
    ListAdapter<ForecastData, HorizontalAdapter.HorizontalViewHolder>(DiffCallback) {

    lateinit var context: Context

    inner class HorizontalViewHolder(private var binding: HorizontalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastData) = with(binding) {
            Timber.tag("rv").d("bind -- viewHolder")
            ivWeather.setImage(item.icon)
            tvTemperature.text = context.resources.getString(R.string.temperature, item.temp)
            tvTime.text = item.time
            binding.root.context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder {
        context = parent.context
        Timber.tag("rv").d("onCreateViewHolder")
        return HorizontalViewHolder(
            HorizontalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) {
        Timber.tag("rv").d("onBindViewHolder")
        val item = getItem(position) as ForecastData
        holder.bind(item)
    }

    companion object {

        val DiffCallback = object : DiffUtil.ItemCallback<ForecastData>() {
            override fun areItemsTheSame(oldItem: ForecastData, newItem: ForecastData): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: ForecastData, newItem: ForecastData): Boolean {
                return oldItem == newItem
            }
        }
    }
}