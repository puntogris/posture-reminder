package com.puntogris.posture.ui.exercise

import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogCompletedExerciseBinding
import com.puntogris.posture.domain.model.RewardExp
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_EXERCISE
import com.puntogris.posture.utils.playAnimationOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseCompletedDialog : DialogFragment() {

    private val viewModel: ExerciseViewModel by viewModels()
    private lateinit var binding: DialogCompletedExerciseBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_completed_exercise, null, false)

        lifecycleScope.launch {
            when (viewModel.updateDayLogWithReward()) {
                RewardExp.Error -> {
                    with(binding) {
                        completedExerciseAnimation.playAnimationOnce(R.raw.error)
                        completedExerciseTitle.setText(R.string.an_error_occurred)
                        completedExerciseMessage.setText(R.string.snack_general_error)
                    }
                }
                RewardExp.ExpLimit -> {
                    with(binding) {
                        completedExerciseAnimation.setAnimation(R.raw.trophy)
                        completedExerciseTitle.setText(R.string.completed_exercise_title)
                        completedExerciseMessage.setText(R.string.exercise_complete_dialog_exp_limit_message)
                    }
                }
                RewardExp.Success -> {
                    with(binding) {
                        completedExerciseAnimation.setAnimation(R.raw.congratulations)
                        completedExerciseTitle.setText(R.string.completed_exercise_title)
                        completedExerciseMessage.text =
                            getString(R.string.completed_exercise_message, EXPERIENCE_PER_EXERCISE)
                    }
                }
            }
        }

        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            .setView(binding.root)
            .setPositiveButton(R.string.action_done) { _, _ ->
                dismiss()
            }
            .create()
    }
}