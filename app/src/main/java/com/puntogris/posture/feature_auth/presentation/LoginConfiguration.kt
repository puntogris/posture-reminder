package com.puntogris.posture.feature_auth.presentation

import android.widget.ProgressBar

interface LoginConfiguration {

    fun onLoginStarted()

    fun onLoginError()

    fun onLoginFinished()

    val viewModel: LoginViewModel

    val progressBar: ProgressBar
}