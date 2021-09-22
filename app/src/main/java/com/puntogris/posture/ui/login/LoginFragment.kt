package com.puntogris.posture.ui.login

import androidx.fragment.app.viewModels
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentLoginBinding
import com.puntogris.posture.ui.base.BaseLoginFragment
import com.puntogris.posture.utils.gone
import com.puntogris.posture.utils.launchWebBrowserIntent
import com.puntogris.posture.utils.navigateTo
import com.puntogris.posture.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseLoginFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override val viewModel: LoginViewModel by viewModels()

    override fun initializeViews() {
        super.initializeViews()
        binding.fragment = this
    }

    override fun onLoginStarted() {
        binding.progressBar.visible()
    }

    override fun onLoginFinished() {
        binding.progressBar.gone()
    }

    override fun onLoginError() {
        binding.progressBar.gone()
    }

    fun continueAnonymously(){
        navigateTo(R.id.welcomeFragment)
    }

    fun onLoginProblemsClicked(){
        launchWebBrowserIntent("https://postureapp.puntogris.com/help/")
    }
}