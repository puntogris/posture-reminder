package com.puntogris.posture.feature_main.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.feature_main.data.datasource.local.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStore
) : ViewModel() {

    private val _appVersionStatus = MutableLiveData<Boolean>()
    val appVersionStatus: LiveData<Boolean> = _appVersionStatus

    init {
        viewModelScope.launch {
            if (dataStore.lastVersionCode() < BuildConfig.VERSION_CODE) {
                dataStore.updateLastVersionCode()
                _appVersionStatus.value = true
            }
        }
    }

    suspend fun showLogin() = dataStore.showLoginPref()
}
