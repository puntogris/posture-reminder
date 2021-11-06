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
import com.puntogris.posture.utils.RewardExp
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
                        animationView.playAnimationOnce(R.raw.error)
                        title.text = getString(R.string.an_error_occurred)
                        message.text = getString(R.string.snack_general_error)
                    }
                }
                RewardExp.ExpLimit -> {
                    with(binding) {
                        animationView.setAnimation(R.raw.trophy)
                        title.text = getString(R.string.completed_exercise_title)
                        message.text =
                            getString(R.string.exercise_complete_dialog_exp_limit_message)
                    }
                }
                RewardExp.Success -> {
                    with(binding) {
                        animationView.setAnimation(R.raw.congratulations)
                        title.text = getString(R.string.completed_exercise_title)
                        message.text =
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