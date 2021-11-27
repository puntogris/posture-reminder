package com.puntogris.posture.feature_main.presentation.rankings

import androidx.lifecycle.ViewModel
import com.puntogris.posture.feature_main.domain.repository.RankingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RankingsViewModel @Inject constructor(
    repository: RankingsRepository
) : ViewModel() {

    val rankings = repository.getRankingsWithLimit(30)
}