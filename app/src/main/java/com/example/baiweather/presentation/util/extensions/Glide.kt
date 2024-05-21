package com.example.baiweather.presentation.util.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.baiweather.R

fun ImageView.setImage(url: String?) {
    Glide
        .with(this.context)
        .load(url)
        .placeholder(R.mipmap.ic_launcher)
        .centerCrop()
        .into(this)
}
