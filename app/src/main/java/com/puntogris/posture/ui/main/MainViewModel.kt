package com.puntogris.posture.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.data.datasource.local.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStore
) : ViewModel() {

    val appVersionStatus = channelFlow {
        val isOldVersion = dataStore.lastVersionCode() < BuildConfig.VERSION_CODE
        if (isOldVersion) dataStore.updateLastVersionCode()
        send(isOldVersion)
    }

    suspend fun showLogin() = dataStore.showLoginPref()
}
