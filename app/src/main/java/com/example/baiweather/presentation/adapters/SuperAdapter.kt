package com.example.baiweather.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.baiweather.R
import com.example.baiweather.databinding.SuperItemBinding
import timber.log.Timber

class SuperAdapter : ListAdapter<ListItem, RecyclerView.ViewHolder>(DiffCallback()) {

    private val forecastAdapter by lazy {
        ForecastAdapter()
    }

    private val gridAdapter by lazy {
        GridAdapter()
    }

    companion object {
        private const val TYPE_HORIZONTAL = 1
        private const val TYPE_GRID = 2
        private const val TYPE_VERTICAL = 3
    }

    override fun getItemViewType(position: Int): Int {
        Timber.tag("getItemViewType").d("$position ${getItem(position)} ")

        return when (getItem(position)) {
            is ListItem.Horizontal -> TYPE_HORIZONTAL
            is ListItem.Grid -> TYPE_GRID
            is ListItem.Vertical -> TYPE_VERTICAL
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Timber.tag("submitList").d("gare onCreateViewHolder viewType  ${viewType}")
        return when (viewType) {
            TYPE_HORIZONTAL -> {
                HorizontalViewHolder(
                    SuperItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            TYPE_GRID -> {
                GridViewHolder(
                    SuperItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            TYPE_VERTICAL -> {
                VerticalViewHolder(
                    SuperItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HorizontalViewHolder -> holder.bind(getItem(position) as ListItem.Horizontal)
            is GridViewHolder -> holder.bind(getItem(position) as ListItem.Grid)
            is VerticalViewHolder -> holder.bind(getItem(position) as ListItem.Vertical)
        }
    }

    inner class HorizontalViewHolder(private var binding: SuperItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListItem.Horizontal) {

            Log.d("givi","print")
            Timber.tag("ggtgtgtg").d("horizontal bind")

            binding.tvTitle.text = item.title
            binding.rvItems.apply {
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = forecastAdapter
                Timber.tag("submitList").d("shida hor")
                forecastAdapter.submitList(item.items)
                background = ResourcesCompat.getDrawable(resources, R.drawable.grid_bg, null)
            }
        }
    }

    inner class GridViewHolder(private var binding: SuperItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListItem.Grid) {
            Timber.tag("ggtgtgtg").d("grid bind")

            binding.tvTitle.text = item.title

            binding.rvItems.apply {
                layoutManager =
                    GridLayoutManager(itemView.context,  2)
                adapter = gridAdapter
                Timber.tag("submitList").d("shida grid")
                gridAdapter.submitList(item.items)
            }
        }
    }

    inner class VerticalViewHolder(private var binding: SuperItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListItem.Vertical) {
            val forecastAdapter = ForecastAdapter()
            binding.tvTitle.text = item.title

            binding.rvItems.apply {
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                adapter = forecastAdapter
                Timber.tag("submitList").d("shida vert")
                forecastAdapter.submitList(item.items)
            }
        }
    }

//    class DiffCallback : DiffUtil.ItemCallback<ListItem>() {
//        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
//            Timber.tag("diffutils").d("1 ${oldItem == newItem}")
//
//            return oldItem == newItem
//        }
//
//        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
//            Timber.tag("diffutils").d("2 ${oldItem == newItem}")
//            return oldItem == newItem
//        }
//    }

    class DiffCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            val x = when {
                oldItem is ListItem.Horizontal && newItem is ListItem.Horizontal -> oldItem.id == newItem.id
                oldItem is ListItem.Grid && newItem is ListItem.Grid -> oldItem.id == newItem.id
                oldItem is ListItem.Vertical && newItem is ListItem.Vertical -> oldItem.id == newItem.id
                else -> false}
            Timber.tag("diffutils").d("1 ${x}")

            return when {
                oldItem is ListItem.Horizontal && newItem is ListItem.Horizontal -> oldItem.id == newItem.id
                oldItem is ListItem.Grid && newItem is ListItem.Grid -> oldItem.id == newItem.id
                oldItem is ListItem.Vertical && newItem is ListItem.Vertical -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            Timber.tag("diffutils").d("2 ${oldItem == newItem}")
            return oldItem == newItem
        }
    }
}
