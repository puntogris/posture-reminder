package com.puntogris.posture.ui.exercise

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.model.RewardExp
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_EXERCISE
import com.puntogris.posture.utils.constants.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import com.puntogris.posture.utils.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val repository: DayLogsRepository
) : ViewModel() {

    private var durationTimer: CountDownTimer? = null

    private val _exerciseDurationTimer = MutableLiveData<Int>()
    val exerciseDurationTimer: LiveData<Int> = _exerciseDurationTimer

    fun startExerciseTimerWithDuration(duration: Int) {
        val durationInMillis = duration.toMillis()
        val countInterval = (1000 / PROGRESS_BAR_SMOOTH_OFFSET).toLong()

        durationTimer = object : CountDownTimer(durationInMillis, countInterval) {
            override fun onTick(millisUntilFinished: Long) {
                _exerciseDurationTimer.value =
                    calculateProgressBarProgress(duration, millisUntilFinished)
            }

            override fun onFinish() {
                _exerciseDurationTimer.value = 0
            }
        }
        durationTimer?.start()
    }

    private fun calculateProgressBarProgress(duration: Int, millisUntilFinished: Long): Int {
        val progressBarMax = duration * PROGRESS_BAR_SMOOTH_OFFSET
        return (progressBarMax - (millisUntilFinished * progressBarMax / duration.toMillis())).toInt()
    }

    fun cancelExpirationTimer() {
        durationTimer?.cancel()
    }

    suspend fun updateDayLogWithReward(): RewardExp {
        val log = DayLog(expGained = EXPERIENCE_PER_EXERCISE, exercises = 1)
        return repository.updateDayLogAndUser(log)
    }

    override fun onCleared() {
        durationTimer?.cancel()
        super.onCleared()
    }

}