package com.puntogris.posture.ui.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R

class SelectThemeDialog(
    private val currentTheme: Int,
    private val onPositiveAction: (Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val currentPosition = if (currentTheme == -1) 2 else currentTheme - 1
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.theme)
            .setSingleChoiceItems(R.array.theme_names, currentPosition) { _, position ->
                dismiss()
                onPositiveAction(position)
            }
            .create()
    }
}
