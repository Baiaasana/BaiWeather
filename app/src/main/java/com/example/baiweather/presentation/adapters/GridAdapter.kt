package com.example.baiweather.presentation.adapters

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baiweather.R
import com.example.baiweather.databinding.GridItemBinding
import com.example.baiweather.domain.model.AdditionalInfo

class GridAdapter : ListAdapter<AdditionalInfo, GridAdapter.GridViewHolder>(DiffCallback) {

    lateinit var context: Context

    inner class GridViewHolder(private var binding: GridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdditionalInfo) = with(binding) {
            binding.tvTitle.text = item.title
            when (item.title) {
                "Wind" -> {
                    createSpan(
                        binding.tvInfo, context.resources.getString(
                            R.string.speed,
                            item.info.toFloat()
                        ), "km/h"
                    )
                    ivIcon.setImageResource(R.drawable.wind)
                }

                "Humidity" -> {
                    createSpan(
                        binding.tvInfo, context.resources.getString(
                            R.string.percentage,
                            item.info.toFloat()
                        ), "%"
                    )
                    ivIcon.setImageResource(R.drawable.humidity)
                }

                "Rain" -> {
                    createSpan(
                        binding.tvInfo, context.resources.getString(
                            R.string.rain_intensity,
                            item.info.toFloat()
                        ), "mm/h"
                    )
                    ivIcon.setImageResource(R.drawable.rain)
                }

                "Pressure" -> {
                    createSpan(
                        binding.tvInfo, context.resources.getString(
                            R.string.pressure_intensity,
                            item.info.toFloat()
                        ), "hPa"
                    )
                    ivIcon.setImageResource(R.drawable.pressure)
                }
            }
        }
    }

    private fun createSpan(view: AppCompatTextView, fulltext: String, text: String) {
        val spannableString = SpannableString(fulltext)
        val color = ContextCompat.getColor(context, R.color.primary_text)
        val startIndex = fulltext.indexOf(text)
        if (startIndex != -1) {
            val endIndex = startIndex + text.length
            spannableString.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        view.text = spannableString
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        context = parent.context
        return GridViewHolder(
            GridItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        val DiffCallback = object : DiffUtil.ItemCallback<AdditionalInfo>() {
            override fun areItemsTheSame(
                oldItem: AdditionalInfo,
                newItem: AdditionalInfo
            ): Boolean {
                return oldItem.info == newItem.info
            }

            override fun areContentsTheSame(
                oldItem: AdditionalInfo,
                newItem: AdditionalInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}