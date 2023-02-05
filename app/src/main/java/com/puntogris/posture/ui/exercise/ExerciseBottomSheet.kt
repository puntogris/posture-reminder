package com.puntogris.posture.ui.exercise

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetExerciseBinding
import com.puntogris.posture.utils.constants.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.setupAsFullScreen
import com.puntogris.posture.utils.setExerciseDuration
import com.puntogris.posture.utils.setProgressBarSmoothMax
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ExerciseBottomSheet : BottomSheetDialogFragment() {

    private val args: ExerciseBottomSheetArgs by navArgs()
    private val viewModel: ExerciseViewModel by viewModels()
    private val binding by viewBinding(BottomSheetExerciseBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        setupObservers()
    }

    private fun setupViews() {
        with(binding) {
            exerciseDuration.setExerciseDuration(args.exercise.duration)
            exerciseImage.setImageResource(args.exercise.image)
            exerciseName.setText(args.exercise.title)
            exerciseDescription.setText(args.exercise.summary)
            progressBar.setProgressBarSmoothMax(args.exercise.duration)
            recyclerView.adapter = ExerciseStepsAdapter(
                resources.getStringArray(args.exercise.steps)
            )
        }
    }

    private fun setupListeners() {
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.startExerciseButton.setOnClickListener {
            startExerciseAndShowInProgressUi()
        }
    }

    private fun setupObservers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.exerciseDurationTimer.collectLatest {
                binding.progressBar.progress = it
                if (it == args.exercise.duration * PROGRESS_BAR_SMOOTH_OFFSET) {
                    updateUiAndShowCompletedExerciseDialog()
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setupAsFullScreen(isDraggable = true)
        }
    }

    private fun startExerciseAndShowInProgressUi() {
        viewModel.startExerciseTimer()
        binding.startExerciseButton.apply {
            isEnabled = false
            setText(R.string.in_progress)
        }
    }

    private fun updateUiAndShowCompletedExerciseDialog() {
        findNavController().navigate(R.id.exerciseCompletedDialog)
        binding.startExerciseButton.setText(R.string.finished)
    }

    override fun onDestroyView() {
        viewModel.cancelExpirationTimer()
        super.onDestroyView()
    }
}