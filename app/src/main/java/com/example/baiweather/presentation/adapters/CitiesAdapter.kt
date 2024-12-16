package com.example.baiweather.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baiweather.data.local.model.CityEntity
import com.example.baiweather.databinding.SingleCityBinding
import timber.log.Timber

class CitiesAdapter :
    ListAdapter<CityEntity, CitiesAdapter.CityViewHolder>(DiffCallback) {

    lateinit var onClick: (CityEntity) -> Unit

    inner class CityViewHolder(private var binding: SingleCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CityEntity) = with(binding) {
            tvCity.text = item.name
            tvCountry.text = item.country
            itemView.setOnClickListener {
                onClick.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        Timber.tag("rv").d("onCreateViewHolder")
        return CityViewHolder(
            SingleCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        Timber.tag("rv").d("onBindViewHolder")
        val item = getItem(position) as CityEntity
        holder.bind(item)
    }

    companion object {

        val DiffCallback = object : DiffUtil.ItemCallback<CityEntity>() {
            override fun areItemsTheSame(oldItem: CityEntity, newItem: CityEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CityEntity, newItem: CityEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}