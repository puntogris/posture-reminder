package com.puntogris.posture.ui.exercise

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repo.day_logs.DayLogsRepository
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.model.RewardExp
import com.puntogris.posture.utils.Constants.EXPERIENCE_PER_EXERCISE
import com.puntogris.posture.utils.Constants.PROGRESS_BAR_SMOOTH_OFFSET
import com.puntogris.posture.utils.toMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val logsRepository: DayLogsRepository
): ViewModel() {
    private var durationTimer: CountDownTimer? = null

    private val _exerciseDurationTimer = MutableLiveData<Int>()
    val exerciseDurationTimer: LiveData<Int> = _exerciseDurationTimer

    fun startExerciseTimerWithDuration(duration: Int){
        val durationInMillis = duration.toMillis()
        val countInterval = (1000 / PROGRESS_BAR_SMOOTH_OFFSET).toLong()
        durationTimer = object: CountDownTimer(durationInMillis, countInterval) {
            override fun onTick(millisUntilFinished: Long) {
                _exerciseDurationTimer.value = calculateProgressBarProgress(duration, millisUntilFinished)
            }

            override fun onFinish() {
                _exerciseDurationTimer.value = 0
            }
        }
        durationTimer?.start()
    }

    private fun calculateProgressBarProgress(duration: Int, millisUntilFinished: Long): Int{
        val progressBarMax = duration * PROGRESS_BAR_SMOOTH_OFFSET
        return (progressBarMax - (millisUntilFinished * progressBarMax / duration.toMillis())).toInt()
    }

    fun cancelExpirationTimer(){
        durationTimer?.cancel()
    }

    override fun onCleared() {
        durationTimer?.cancel()
        super.onCleared()
    }

    suspend fun updateDayLogWithReward(): RewardExp{
        val log = DayLog(expGained = EXPERIENCE_PER_EXERCISE, exercises = 1)
        return logsRepository.updateRoomDayLogAndUser(log)
    }

}