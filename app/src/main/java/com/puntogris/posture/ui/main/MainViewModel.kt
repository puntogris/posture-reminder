package com.puntogris.posture.ui.main

import androidx.lifecycle.*
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.data.repo.main.MainRepository
import com.puntogris.posture.utils.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val dataStore: DataStore
    ): ViewModel() {

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

    suspend fun isUserLoggedIn() = mainRepository.isUserLoggedIn() && dataStore.isLoginCompleted()

}