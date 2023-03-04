package com.puntogris.posture.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val NO_SPACING = 0

class GridItemDecorator(private val edgeEnabled: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        if (layoutManager !is GridLayoutManager) {
            return
        }
        val spansCount = layoutManager.spanCount
        val itemWidth = view.layoutParams.width
        val widthForPadding = parent.width - (itemWidth * spansCount)
        val paddingCount = if (edgeEnabled) spansCount.inc() else spansCount.dec()
        val padding = widthForPadding / paddingCount

        makeGridSpacing(
            outRect,
            parent.getChildAdapterPosition(view),
            state.itemCount,
            layoutManager.spanCount,
            layoutManager.reverseLayout,
            padding
        )
    }

    private fun makeGridSpacing(
        outRect: Rect,
        position: Int,
        itemCount: Int,
        spanCount: Int,
        isReversed: Boolean,
        padding: Int
    ) {
        val sizeBasedOnEdge = if (edgeEnabled) padding else NO_SPACING

        // Opposite of spanCount (find the list depth)
        val subsideCount = if (itemCount % spanCount == 0) {
            itemCount / spanCount
        } else {
            (itemCount / spanCount) + 1
        }

        // Grid position. Imagine all items ordered in x/y axis
        val xAxis = position % spanCount
        val yAxis = position / spanCount

        // Conditions in row and column
        val isFirstRow = yAxis == 0
        val isLastRow = yAxis == subsideCount - 1

        // Saved size
        val sizeBasedOnFirstRow = if (isFirstRow) sizeBasedOnEdge else NO_SPACING
        val sizeBasedOnLastRow = if (!isLastRow) padding else sizeBasedOnEdge
        with(outRect) {
            left = if (edgeEnabled) {
                padding * (spanCount - xAxis) / (spanCount)
            } else {
                padding * xAxis / spanCount
            }
            right = if (edgeEnabled) {
                padding * (xAxis + 1) / spanCount
            } else {
                padding * (spanCount - (xAxis + 1)) / spanCount
            }
            top = if (isReversed) sizeBasedOnLastRow else sizeBasedOnFirstRow
            bottom = if (isReversed) sizeBasedOnFirstRow else sizeBasedOnLastRow
        }
    }
}
