package com.puntogris.posture.ui.portal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.domain.repository.RankingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PortalViewModel @Inject constructor(
    repository: RankingsRepository
) : ViewModel() {

    val rankings = repository.getRankingsWithLimit(3)
}