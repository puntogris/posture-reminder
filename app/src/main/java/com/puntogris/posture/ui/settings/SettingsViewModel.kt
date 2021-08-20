package com.puntogris.posture.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.posture.data.local.UserDao
import com.puntogris.posture.data.repo.login.LoginRepository
import com.puntogris.posture.data.repo.user.UserRepository
import com.puntogris.posture.utils.DataStore
import com.puntogris.posture.utils.capitalizeWords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStore: DataStore,
    private val loginRepository: LoginRepository
    ): ViewModel() {

    suspend fun updateUserName(name: String) = userRepository.updateUsernameInRoomAndFirestore(name.capitalizeWords())

    fun getUserFlow() = userRepository.getUserFlowRoom()

    fun getThemeNamePosition() = dataStore.appThemeFlow()

    fun setPandaAnimationPref(value:Boolean){
        viewModelScope.launch {
            dataStore.setPandaAnimation(value)
        }
    }

    fun logOut() = loginRepository.signOutUserFromFirebaseAndGoogle()
}