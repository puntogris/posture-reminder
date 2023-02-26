package com.puntogris.posture.ui.account

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.domain.repository.DayLogsRepository
import com.puntogris.posture.domain.repository.UserRepository
import com.puntogris.posture.utils.constants.Constants.EXPERIENCE_TO_DISABLE_CHECKPOINT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    userRepository: UserRepository,
    dayLogsRepository: DayLogsRepository,
    dataStoreHelper: DataStoreHelper
) : ViewModel() {

    val user = userRepository.getUserStream()

    val weekData = dayLogsRepository.getWeekDayLogsStream()

    val showCheckpoint = combine(user, dataStoreHelper.expNeededToShowCheckpoint()) { user, expToShow ->
        expToShow != EXPERIENCE_TO_DISABLE_CHECKPOINT && user.experience > expToShow
    }
}
