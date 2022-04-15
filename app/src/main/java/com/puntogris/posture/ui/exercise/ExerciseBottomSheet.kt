package com.puntogris.posture.ui.exercise

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetExerciseBinding
import com.puntogris.posture.utils.constants.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import com.puntogris.posture.utils.extensions.navigateTo
import com.puntogris.posture.utils.extensions.setupAsFullScreen
import com.puntogris.posture.utils.setExerciseDuration
import com.puntogris.posture.utils.setProgressBarSmoothMax
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseBottomSheet : BottomSheetDialogFragment() {

    private val args: ExerciseBottomSheetArgs by navArgs()
    private val viewModel: ExerciseViewModel by viewModels()
    private val binding by viewBinding(BottomSheetExerciseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewAdapter()

        binding.exerciseImage.setImageResource(args.exercise.image)
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.startExerciseButton.setOnClickListener {
            startExercise()
        }
        viewModel.exerciseDurationTimer.observe(viewLifecycleOwner) {
            binding.exerciseDuration.setExerciseDuration(it)
            binding.progressBar.setProgressBarSmoothMax(it)
            binding.progressBar.progress = it
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setupAsFullScreen(isDraggable = true)
        }
    }

    private fun setupRecyclerViewAdapter() {
        val steps = resources.getStringArray(args.exercise.steps)
        binding.recyclerView.adapter = ExerciseStepsAdapter(steps)
    }

    private fun startExercise() {
        viewModel.startExerciseTimerWithDuration(args.exercise.duration)
        setCompleteExerciseListener()
        showInProgressUi()
    }

    private fun showInProgressUi() {
        binding.startExerciseButton.apply {
            isEnabled = false
            text = context.getString(R.string.in_progress)
        }
    }

    private fun setCompleteExerciseListener() {
        viewModel.exerciseDurationTimer.observe(viewLifecycleOwner) {
            if (it == args.exercise.duration * PROGRESS_BAR_SMOOTH_OFFSET) {
                updateUiAndShowCompletedExerciseDialog()
            }
        }
    }

    private fun updateUiAndShowCompletedExerciseDialog() {
        navigateTo(R.id.exerciseCompletedDialog)
        binding.startExerciseButton.text = getString(R.string.finished)
    }

    override fun onDestroyView() {
        viewModel.cancelExpirationTimer()
        super.onDestroyView()
    }
}