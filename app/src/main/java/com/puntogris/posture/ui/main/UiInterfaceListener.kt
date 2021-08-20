package com.puntogris.posture.ui.main

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.puntogris.posture.R

interface UiInterfaceListener {
    fun showSnackBar(@StringRes message: Int,
                     duration: Int = Snackbar.LENGTH_LONG,
                     actionText: Int = R.string.action_undo,
                     actionListener: View.OnClickListener? = null)
}