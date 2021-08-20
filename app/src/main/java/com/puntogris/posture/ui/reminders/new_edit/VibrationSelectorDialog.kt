package com.puntogris.posture.ui.reminders.new_edit

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.utils.Constants.DATA_KEY
import com.puntogris.posture.utils.Constants.VIBRATION_PICKER_KEY
import com.puntogris.posture.utils.Utils.vibrationPatterns

class VibrationSelectorDialog : DialogFragment() {

    private val args: VibrationSelectorDialogArgs by navArgs()
    private var vibrator: Vibrator? = null
    private var vibrationPosition = 0

    @Suppress("DEPRECATION")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            .setTitle(R.string.vibration_picker_title)
            .setPositiveButton(R.string.action_done) { _, _ ->
                setFragmentResult(VIBRATION_PICKER_KEY, bundleOf(DATA_KEY to vibrationPosition))
                findNavController().navigateUp()
            }
            .setNegativeButton(R.string.action_cancel) { _, _ -> dismiss() }
            .setSingleChoiceItems(R.array.vibrationPatterns, args.savedPosition) { _, position ->

                if (position != 0) {
                    val pattern = vibrationPatterns[position]
                    vibrator?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                            it.vibrate(VibrationEffect.createWaveform(pattern, -1))
                        else it.vibrate(pattern, -1)
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


