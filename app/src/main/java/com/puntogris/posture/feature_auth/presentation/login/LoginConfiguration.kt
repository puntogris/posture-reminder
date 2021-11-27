package com.puntogris.posture.feature_auth.presentation.login

import android.widget.ProgressBar

interface LoginConfiguration {

    fun onLoginStarted()

    fun onLoginError()

    fun onLoginFinished()

    val viewModel: LoginViewModel

    val progressBar: ProgressBar
}