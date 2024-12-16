package com.example.baiweather.presentation.util.extensions

import androidx.recyclerview.widget.RecyclerView

fun resetItemDecoration(recyclerView: RecyclerView) {
    while (recyclerView.itemDecorationCount > 0) {
        recyclerView.removeItemDecorationAt(0)
    }
}