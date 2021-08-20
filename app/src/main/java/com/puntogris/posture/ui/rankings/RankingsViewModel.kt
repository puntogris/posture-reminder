package com.puntogris.posture.ui.rankings

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repo.rankings.RankingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RankingsViewModel @Inject constructor(
    private val rankingsRepository: RankingsRepository
): ViewModel(){

    suspend fun getAllRankings() = rankingsRepository.getAllRankingsFirestore()
}