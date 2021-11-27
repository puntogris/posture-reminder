package com.puntogris.posture.feature_main.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.feature_main.data.datasource.local.DataStore
import com.puntogris.posture.feature_auth.domain.repository.AuthRepository
import com.puntogris.posture.feature_main.domain.repository.UserRepository
import com.puntogris.posture.common.utils.capitalizeWords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStore: DataStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    val user = userRepository.getUserLiveData()

    val appTheme = dataStore.appThemeFlow()

    fun setPandaAnimationPref(value: Boolean) {
        viewModelScope.launch {
            dataStore.setPandaAnimation(value)
        }
    }

    fun isUserLoggedIn() = userRepository.isUserLoggedIn()

    suspend fun updateUserName(name: String) = userRepository.updateUsername(name.capitalizeWords())

    suspend fun logOut() = authRepository.signOutUser()

}