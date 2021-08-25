package com.puntogris.posture.ui.main

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R

interface UiInterfaceListener {
    fun showSnackBar(message: String,
                     duration: Int = Snackbar.LENGTH_LONG,
                     actionText: Int = R.string.action_undo,
                     anchorToBottomNav: Boolean = true,
                     actionListener: View.OnClickListener? = null)
}