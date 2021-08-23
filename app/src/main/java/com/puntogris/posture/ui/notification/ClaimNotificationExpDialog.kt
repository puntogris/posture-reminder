package com.puntogris.posture.ui.notification

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.data.local.LocalDataSource
import com.puntogris.posture.utils.Constants.EXPERIENCE_PER_NOTIFICATION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClaimNotificationExpDialog: DialogFragment(){

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lifecycleScope.launch {
            viewModel.updateDayLogWithReward()
        }

        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
            .setTitle(R.string.claim_experience_title)
            .setMessage(getString(
                R.string.claim_experience_message,
                EXPERIENCE_PER_NOTIFICATION
            ))
            .setView(R.layout.dialog_claim_experience)
            .setPositiveButton(R.string.exercise) { _, _ ->
                dismiss()
                navigateToRandomExercise()
            }
            .setNegativeButton(R.string.action_cancel){_, _ -> dismiss()}
            .create()
    }

    private fun navigateToRandomExercise(){
        val exercise = LocalDataSource().exercisesList.random()
        val action = ClaimNotificationExpDialogDirections
            .actionClaimNotificationExpDialogToExerciseBottomSheet(exercise)
        findNavController().navigate(action)
    }
}