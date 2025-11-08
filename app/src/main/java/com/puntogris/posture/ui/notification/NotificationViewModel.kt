package com.puntogris.posture.ui.notification

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.domain.model.DayLog
import com.puntogris.posture.domain.model.RewardExp
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.repository.SyncRepository
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_PER_NOTIFICATION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val logsRepository: DayLogsRepository,
    private val syncRepository: SyncRepository,
    dataStoreHelper: DataStoreHelper
) : ViewModel() {

    val expRewardStatus = flow {
        val log = DayLog(expGained = EXPERIENCE_PER_NOTIFICATION, notifications = 1)
        emit(logsRepository.updateDayLogAndUser(log))
    }.onEach {
        if (it is RewardExp.Success) {
            syncRepository.syncAccountExperience()
        }
    }

    val playExpSound = dataStoreHelper.isExpSoundEnabled()
}
