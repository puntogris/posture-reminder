package com.puntogris.posture.ui.portal

import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.repository.RankingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PortalViewModel @Inject constructor(
    private val rankingsRepository: RankingsRepository
): ViewModel() {

    suspend fun getTopThreeRankings() = rankingsRepository.getTopThreeRankingsServer()

}