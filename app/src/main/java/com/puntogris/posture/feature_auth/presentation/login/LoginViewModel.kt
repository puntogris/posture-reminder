package com.puntogris.posture.feature_auth.presentation.login

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.puntogris.posture.feature_auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    fun authGoogleUser(result: ActivityResult) = authRepository.authWithGoogle(result)

    fun getGoogleSignInIntent() = googleSignInClient.signInIntent
}