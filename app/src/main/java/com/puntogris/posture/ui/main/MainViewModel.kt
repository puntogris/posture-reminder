package com.puntogris.posture.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.data.datasource.local.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStore
) : ViewModel() {

    val appVersionStatus = liveData {
        val isOldVersion = dataStore.lastVersionCode() < BuildConfig.VERSION_CODE
        if (isOldVersion) dataStore.updateLastVersionCode()
        emit(isOldVersion)
    }

    suspend fun showLogin() = dataStore.showLoginPref()
}
