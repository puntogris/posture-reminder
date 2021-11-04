package com.puntogris.posture.ui.login

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.puntogris.posture.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
):ViewModel() {

    fun authGoogleUser(result: ActivityResult) = loginRepository.serverAuthWithGoogle(result)

    fun getGoogleSignInIntent() = loginRepository.getGoogleSignInIntent()

    suspend fun registerAnonymousUser() = loginRepository.singInAnonymously()
}