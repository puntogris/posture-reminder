package com.puntogris.posture.ui.login

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.puntogris.posture.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    fun authGoogleUser(result: ActivityResult) = loginRepository.serverAuthWithGoogle(result)

    fun getGoogleSignInIntent() = googleSignInClient.signInIntent

    suspend fun registerAnonymousUser() = loginRepository.singInAnonymously()
}