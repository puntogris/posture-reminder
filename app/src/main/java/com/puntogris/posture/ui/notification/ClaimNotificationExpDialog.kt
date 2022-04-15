package com.puntogris.posture.ui.notification

import android.app.Dialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.LocalDataSource
import com.puntogris.posture.databinding.DialogClaimExperienceBinding
import com.puntogris.posture.domain.model.RewardExp
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_NOTIFICATION
import com.puntogris.posture.utils.extensions.playAnimationOnce
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClaimNotificationExpDialog : DialogFragment() {

    private val binding by viewBinding(DialogClaimExperienceBinding::inflate)
    private val viewModel: NotificationViewModel by viewModels()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.claim_sound)
        mediaPlayer?.start()
        lifecycleScope.launch {
            when (viewModel.updateDayLogWithReward()) {
                RewardExp.Error -> {
                    with(binding) {
                        claimExpAnimation.playAnimationOnce(R.raw.error)
                        claimExpTitle.setText(R.string.an_error_occurred)
                        claimExpSummary.setText(R.string.claim_experience_error_summary)
                        claimExpMessage.setText(R.string.snack_general_error)
                    }
                }
                RewardExp.ExpLimit -> {
                    with(binding) {
                        claimExpAnimation.setAnimation(R.raw.trophy)
                        claimExpTitle.setText(R.string.claim_experience_title)
                        claimExpSummary.setText(R.string.claim_experience_exp_limit_summary)
                        claimExpMessage.setText(R.string.claim_experience_exp_limit_message)
                    }
                }
                RewardExp.Success -> {
                    with(binding) {
                        claimExpAnimation.setAnimation(R.raw.congratulations)
                        claimExpTitle.setText(R.string.completed_exercise_title)
                        claimExpSummary.text = getString(
                            R.string.claim_experience_message,
                            EXPERIENCE_PER_NOTIFICATION
                        )
                        claimExpMessage.setText(R.string.claim_experience_exercise_message)
                    }
                }
            }
        }

        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            .setView(binding.root)
            .setPositiveButton(R.string.exercise) { _, _ ->
                navigateToRandomExercise()
            }
            .setNegativeButton(R.string.action_cancel) { _, _ -> dismiss() }
            .create()
    }

    private fun navigateToRandomExercise() {
        val exercise = LocalDataSource.exercisesList.random()
        val action = ClaimNotificationExpDialogDirections
            .actionClaimNotificationExpToExercise(exercise)
        findNavController().navigate(action)
    }

    override fun onDismiss(dialog: DialogInterface) {
        mediaPlayer?.stop()
        super.onDismiss(dialog)
    }
}