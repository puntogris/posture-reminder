package com.puntogris.posture.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.domain.repository.AuthRepository
import com.puntogris.posture.domain.repository.UserRepository
import com.puntogris.posture.utils.extensions.capitalizeWords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreHelper: DataStoreHelper,
    private val authRepository: AuthRepository
) : ViewModel() {

    val user = userRepository.getUserLiveData()

    val appTheme = dataStoreHelper.appThemeFlow()

    fun setPandaAnimationPref(value: Boolean) {
        viewModelScope.launch {
            dataStoreHelper.setPandaAnimation(value)
        }
    }

    fun isUserLoggedIn() = userRepository.isUserLoggedIn()

    suspend fun updateUserName(name: String) = userRepository.updateUsername(name.capitalizeWords())

    suspend fun logOut() = authRepository.signOutUser()

}