package com.puntogris.posture.feature_main.presentation.exercise

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetExerciseBinding
import com.puntogris.posture.common.presentation.base.BaseBindingBottomSheetFragment
import com.puntogris.posture.common.utils.constants.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import com.puntogris.posture.common.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseBottomSheet : BaseBindingBottomSheetFragment<BottomSheetExerciseBinding>(
    R.layout.bottom_sheet_exercise,
    true
) {

    private val args: ExerciseBottomSheetArgs by navArgs()
    private val viewModel: ExerciseViewModel by viewModels()

    override fun initializeViews() {
        binding.let {
            it.bottomSheet = this
            it.exercise = args.exercise
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }

        setupRecyclerViewAdapter()
    }

    private fun setupRecyclerViewAdapter() {
        val steps = resources.getStringArray(args.exercise.steps)
        binding.recyclerView.adapter = ExerciseStepsAdapter(steps)
    }

    fun startExercise() {
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