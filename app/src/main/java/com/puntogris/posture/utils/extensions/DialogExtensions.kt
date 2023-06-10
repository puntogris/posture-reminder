package com.puntogris.posture.utils.extensions

import android.app.Dialog
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R

inline fun AlertDialog.onPositive(crossinline block: () -> Unit) {
    setOnShowListener {
        getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            block()
        }
    }
}

fun BottomSheetDialog.setupAsFullScreen(isDraggable: Boolean = false): Dialog {
    return this.apply {
        window?.let {
            behavior.isDraggable = isDraggable
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            WindowInsetsControllerCompat(it, it.decorView)
                .isAppearanceLightStatusBars = !context.isDarkThemeOn()
        }
        behavior.skipCollapsed = true

        setOnShowListener {
            findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let { layout ->
                setupFullHeight(layout)
            }
        }
    }
}

private fun setupFullHeight(bottomSheet: View) {
    val layoutParams = bottomSheet.layoutParams
    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
    bottomSheet.layoutParams = layoutParams
    BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
}

fun BottomSheetDialogFragment.showSnackBar(
    @StringRes message: Int,
    duration: Int = Snackbar.LENGTH_LONG,
    anchorView: View? = null,
    actionText: Int = R.string.action_undo,
    actionListener: View.OnClickListener? = null
) {
    requireDialog().window?.decorView?.let {
        Snackbar.make(it, message, duration).apply {
            if (anchorView != null) {
                this.anchorView = anchorView
            }
            if (actionListener != null) {
                this.setAction(actionText, actionListener)
            }
        }.show()
    }
}
