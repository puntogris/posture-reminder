package com.puntogris.posture.ui.base

import com.puntogris.posture.ui.login.LoginViewModel

interface LoginConfiguration{
    fun onLoginStarted()
    fun onLoginError()
    fun onLoginFinished()
    val viewModel: LoginViewModel
}