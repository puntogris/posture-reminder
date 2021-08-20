package com.puntogris.posture.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R

abstract class SwipeToDeleteCallback(context: Context):
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val textPaint = TextPaint().also {
        it.isAntiAlias = true
        it.color = Color.GRAY
        it.textSize = 40F
    }
    private val text = context.getString(R.string.action_delete)

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val margin = 60f
        val textWidth = textPaint.measureText(text)
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val textLeftX = itemView.left + itemView.paddingRight.toFloat() + margin
        val textRightX = itemView.right - itemView.paddingRight - textWidth - margin
        val textY = itemView.top + itemHeight / 2f

        c.drawText(text, textRightX, textY, textPaint)
        c.drawText(text, textLeftX, textY, textPaint)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder) = false
}