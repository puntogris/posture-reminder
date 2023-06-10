package com.puntogris.posture.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.TextPaint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.puntogris.posture.R

private const val TEXT_SIZE = 45F
private const val TEXT_MARGIN = 120F
private const val ICON_MARGIN = 30

abstract class SwipeToDeleteCallback(val context: Context) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        color = Color.WHITE
        textSize = TEXT_SIZE
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val textBounds = Rect()

    private val deleteText = context.getString(R.string.action_delete)

    private val editText = context.getString(R.string.action_edit)

    private val deleteIcon by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_trash)?.apply {
            setTint(Color.WHITE)
        }
    }

    private val editIcon by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_pencil_fill)?.apply {
            setTint(Color.WHITE)
        }
    }

    private val iconSize = context.resources.getDimensionPixelSize(R.dimen.spacing_6)

    private val background = ColorDrawable()

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        drawBackground(canvas, itemView, dX.toInt())
        drawIcon(canvas, itemView, dX.toInt())
        drawText(canvas, itemView, dX.toInt())

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawBackground(canvas: Canvas, itemView: View, xPosition: Int) {
        background.apply {
            if (xPosition > 0) {
                color = ContextCompat.getColor(context, R.color.delete)
                setBounds(itemView.left, itemView.top, xPosition, itemView.bottom)
            } else {
                color = ContextCompat.getColor(context, R.color.card2)
                setBounds(itemView.right + xPosition, itemView.top, itemView.right, itemView.bottom)
            }
        }
        background.draw(canvas)
    }

    private fun drawIcon(canvas: Canvas, view: View, xPosition: Int) {
        val middleHeight = iconSize / 2
        val yPos = getViewCenter(view)
        val top = yPos - middleHeight
        val bottom = yPos + middleHeight

        if (xPosition > 0) {
            val left = view.left + ICON_MARGIN
            val right = left + iconSize
            deleteIcon?.setBounds(left, top, right, bottom)
            deleteIcon?.draw(canvas)
        } else {
            val right = view.right - ICON_MARGIN
            val left = right - iconSize
            editIcon?.setBounds(left, top, right, bottom)
            editIcon?.draw(canvas)
        }
    }

    private fun drawText(canvas: Canvas, view: View, xPosition: Int) {
        val textY = getViewCenter(view) - textBounds.exactCenterY()
        if (xPosition > 0) {
            textPaint.getTextBounds(deleteText, 0, deleteText.length, textBounds)
            canvas.drawText(deleteText, view.left + TEXT_MARGIN, textY, textPaint)
        } else {
            val textWidth = textPaint.measureText(editText)
            val textRightX = view.right - textWidth - TEXT_MARGIN
            textPaint.getTextBounds(editText, 0, editText.length, textBounds)
            canvas.drawText(editText, textRightX, textY, textPaint)
        }
    }

    private fun getViewCenter(view: View) = view.top + view.height / 2

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false
}
