package com.puntogris.posture.ui.exercise

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.utils.Constants.EXPERIENCE_PER_EXERCISE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseCompletedDialog: DialogFragment()  {

    private val viewModel: ExerciseViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lifecycleScope.launch {
            viewModel.updateDayLog()
        }
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