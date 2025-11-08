package com.puntogris.posture.ui.exercises

import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.model.Exercise
import com.puntogris.posture.domain.model.RewardExp
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.repository.SyncRepository
import com.puntogris.posture.utils.constants.Constants.DEFAULT_EXERCISE_TIMER_SECS
import com.puntogris.posture.utils.constants.Constants.EXERCISE_KEY
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_EXERCISE
import com.puntogris.posture.utils.constants.Constants.ONE_SECOND_IN_MILLIS
import com.puntogris.posture.utils.constants.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import com.puntogris.posture.utils.extensions.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: DayLogsRepository,
    private val syncRepository: SyncRepository,
    handle: SavedStateHandle,
    dataStoreHelper: DataStoreHelper
) : ViewModel() {

    private val _exerciseDurationTimer = MutableStateFlow(0)
    val exerciseDurationTimer: StateFlow<Int> = _exerciseDurationTimer

    val expRewardStatus = flow {
        val log = DayLog(expGained = EXPERIENCE_PER_EXERCISE, exercises = 1)
        emit(repository.updateDayLogAndUser(log))
    }.onEach {
        if (it is RewardExp.Success) {
            syncRepository.syncAccountExperience()
        }
    }

    val playExerciseSound = dataStoreHelper.isExerciseSoundEnabled()

    private val durationTimer: CountDownTimer by lazy {
        val duration = handle.get<Exercise>(EXERCISE_KEY)?.duration ?: DEFAULT_EXERCISE_TIMER_SECS
        val durationInMillis = duration.toMillis()
        val countInterval = (ONE_SECOND_IN_MILLIS / PROGRESS_BAR_SMOOTH_OFFSET).toLong()

        object : CountDownTimer(durationInMillis, countInterval) {
            override fun onTick(millisUntilFinished: Long) {
                viewModelScope.launch {
                    _exerciseDurationTimer.value = calculateExerciseBarProgress(
                        duration,
                        millisUntilFinished
                    )
                }
            }
            override fun onFinish() {
                _exerciseDurationTimer.value = 0
            }
        }
    }

    private fun calculateExerciseBarProgress(duration: Int, millisUntilFinished: Long): Int {
        val progressBarMax = duration * PROGRESS_BAR_SMOOTH_OFFSET
        return (progressBarMax - (millisUntilFinished * progressBarMax / duration.toMillis())).toInt()
    }

    fun startExerciseTimer() {
        durationTimer.start()
    }

    fun cancelExpirationTimer() {
        durationTimer.cancel()
    }

    override fun onCleared() {
        durationTimer.cancel()
        super.onCleared()
    }
}
