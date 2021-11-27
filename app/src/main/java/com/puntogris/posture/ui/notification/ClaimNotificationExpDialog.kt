package com.puntogris.posture.ui.notification

import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.LocalDataSource
import com.puntogris.posture.databinding.DialogClaimExperienceBinding
import com.puntogris.posture.domain.model.RewardExp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClaimNotificationExpDialog : DialogFragment() {

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var binding: DialogClaimExperienceBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_claim_experience, null, false)
        lifecycleScope.launch {
            when (viewModel.updateDayLogWithReward()) {
                RewardExp.Error -> {
                    with(binding) {
                        claimExpAnimation.playAnimationOnce(R.raw.error)
                        claimExpMessage.setText(R.string.an_error_occurred)
                        claimExpSummary.setText(R.string.claim_experience_error_summary)
                        claimExpMessage.setText(R.string.snack_general_error)
                    }
                }
                RewardExp.ExpLimit -> {
                    with(binding) {
                        claimExpAnimation.setAnimation(R.raw.trophy)
                        claimExpMessage.setText(R.string.claim_experience_title)
                        claimExpSummary.setText(R.string.claim_experience_exp_limit_summary)
                        claimExpMessage.setText(R.string.claim_experience_exp_limit_message)
                    }
                }
                RewardExp.Success -> {
                    with(binding) {
                        claimExpAnimation.setAnimation(R.raw.congratulations)
                        claimExpMessage.setText(R.string.completed_exercise_title)
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
}