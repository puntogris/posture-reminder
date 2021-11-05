package com.puntogris.posture.ui.login

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentLoginBinding
import com.puntogris.posture.ui.base.BaseLoginFragment
import com.puntogris.posture.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseLoginFragment<FragmentLoginBinding>(R.layout.fragment_login) {

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

    fun continueAnonymously() {
        onLoginStarted()
        lifecycleScope.launch {
            when (viewModel.registerAnonymousUser()) {
                SimpleResult.Failure -> {
                    UiInterface.showSnackBar(getString(R.string.snack_general_error))
                    onLoginError()
                }
                SimpleResult.Success -> navigateTo(R.id.welcomeFragment)
            }
        }
    }

    fun onLoginProblemsClicked() {
        launchWebBrowserIntent("https://postureapp.puntogris.com/help/")
    }
}