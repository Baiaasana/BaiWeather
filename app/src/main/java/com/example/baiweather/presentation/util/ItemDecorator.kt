package com.example.baiweather.presentation.util

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecorator(private val margin: Int, val vertical: Boolean? = true) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        // int to px
        val rightMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            margin.toFloat(),
            parent.resources.displayMetrics
        ).toInt()

        if (vertical == true) {
            outRect.bottom = rightMarginPx
            outRect.bottom = rightMarginPx

        } else {
            if (itemPosition != parent.adapter!!.itemCount - 1) {
                outRect.right = rightMarginPx
            }
        }
    }
}