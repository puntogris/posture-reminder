package com.puntogris.posture.ui.exercise

import android.app.Dialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogCompletedExerciseBinding
import com.puntogris.posture.domain.model.RewardExp
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_EXERCISE
import com.puntogris.posture.utils.extensions.playAnimationOnce
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseCompletedDialog : DialogFragment() {

    private val binding by viewBinding(DialogCompletedExerciseBinding::inflate)
    private val viewModel: ExerciseViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.claim_sound)
        mediaPlayer?.start()
        subscribeUi()
        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.action_done) { _, _ ->
                dismiss()
            }
            .create()
    }

    private fun subscribeUi() {
        lifecycleScope.launch {
            viewModel.expRewardStatus.collectLatest {
                when (it) {
                    RewardExp.Error -> onExpRewardError()
                    RewardExp.ExpLimit -> onExpRewardLimit()
                    RewardExp.Success -> onExpRewardSuccess()
                }
            }
        }
    }

    private fun onExpRewardError() {
        with(binding) {
            completedExerciseAnimation.playAnimationOnce(R.raw.error)
            completedExerciseTitle.setText(R.string.an_error_occurred)
            completedExerciseMessage.setText(R.string.snack_general_error)
        }
    }

    private fun onExpRewardLimit() {
        with(binding) {
            completedExerciseAnimation.setAnimation(R.raw.trophy)
            completedExerciseTitle.setText(R.string.completed_exercise_title)
            completedExerciseMessage.setText(R.string.exercise_complete_dialog_exp_limit_message)
        }
    }

    private fun onExpRewardSuccess() {
        with(binding) {
            completedExerciseAnimation.setAnimation(R.raw.congratulations)
            completedExerciseTitle.setText(R.string.completed_exercise_title)
            completedExerciseMessage.text =
                getString(R.string.completed_exercise_message, EXPERIENCE_PER_EXERCISE)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        mediaPlayer?.stop()
        super.onDismiss(dialog)
    }
}
