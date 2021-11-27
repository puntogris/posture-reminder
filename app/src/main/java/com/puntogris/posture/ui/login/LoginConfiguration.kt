package com.puntogris.posture.ui.login

import android.widget.ProgressBar

interface LoginConfiguration {

    fun onLoginStarted()

    fun onLoginError()

    fun onLoginFinished()

    val viewModel: LoginViewModel

    val progressBar: ProgressBar
}