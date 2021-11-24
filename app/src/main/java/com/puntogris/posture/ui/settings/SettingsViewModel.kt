package com.puntogris.posture.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.domain.repository.AuthRepository
import com.puntogris.posture.domain.repository.UserRepository
import com.puntogris.posture.utils.capitalizeWords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStore: DataStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    suspend fun updateUserName(name: String) = userRepository.updateUsername(name.capitalizeWords())

    val user = userRepository.getUserLiveData()

    fun getThemeNamePosition() = dataStore.appThemeFlow()

    fun setPandaAnimationPref(value: Boolean) {
        viewModelScope.launch {
            dataStore.setPandaAnimation(value)
        }
    }

    fun isUserLoggedIn() = userRepository.isUserLoggedIn()

    suspend fun logOut() = authRepository.signOutUser()

}