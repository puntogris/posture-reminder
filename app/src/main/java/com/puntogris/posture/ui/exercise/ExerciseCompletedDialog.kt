package com.puntogris.posture.ui.exercise

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.utils.Constants.EXPERIENCE_PER_EXERCISE

class ExerciseCompletedDialog: DialogFragment()  {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            .setTitle(R.string.completed_exercise_title)
            .setMessage(getString(R.string.completed_exercise_message, EXPERIENCE_PER_EXERCISE))
            .setView(R.layout.dialog_completed_exercise)
            .setPositiveButton(R.string.action_done) { _, _ ->
                dismiss()
            }
            .create()
    }
}