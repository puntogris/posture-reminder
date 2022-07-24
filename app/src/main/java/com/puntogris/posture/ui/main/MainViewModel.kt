package com.puntogris.posture.ui.main

import androidx.lifecycle.ViewModel
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {

    val appVersionStatus = channelFlow {
        val isOldVersion = dataStoreHelper.lastVersionCode() < BuildConfig.VERSION_CODE
        if (isOldVersion) dataStoreHelper.updateLastVersionCode()
        send(isOldVersion)
    }

    suspend fun showLogin() = dataStoreHelper.showLoginPref()
}
