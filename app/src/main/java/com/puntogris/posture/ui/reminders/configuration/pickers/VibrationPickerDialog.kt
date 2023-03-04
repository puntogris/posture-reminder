package com.puntogris.posture.ui.reminders.configuration.pickers

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.LocalDataSource

class VibrationPickerDialog(
    private val currentPosition: Int,
    private val onPickedAction: (Int) -> Unit
) : DialogFragment() {

    private var vibrator: Vibrator? = null
    private var vibrationPosition = 0

    @Suppress("DEPRECATION")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        val vibrationPatterns = LocalDataSource.vibrationPatterns
        val patternsTittleList = vibrationPatterns.map { getString(it.title) }.toTypedArray()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.vibration_picker_title)
            .setPositiveButton(R.string.action_done) { _, _ ->
                onPickedAction(vibrationPosition)
            }
            .setNegativeButton(R.string.action_cancel) { _, _ -> dismiss() }
            .setSingleChoiceItems(patternsTittleList, currentPosition) { _, position ->
                if (position != 0) {
                    val pattern = vibrationPatterns[position].pattern
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
                    } else {
                        vibrator?.vibrate(pattern, -1)
                    }
                }
                vibrationPosition = position
            }
            .create()
    }

    override fun dismiss() {
        vibrator?.cancel()
        super.dismiss()
    }
}


