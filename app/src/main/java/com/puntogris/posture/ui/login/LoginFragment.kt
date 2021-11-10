package com.puntogris.posture.ui.login

import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentLoginBinding
import com.puntogris.posture.ui.base.BaseLoginFragment
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.UiInterface
import com.puntogris.posture.utils.launchWebBrowserIntent
import com.puntogris.posture.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseLoginFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override val viewModel: LoginViewModel by viewModels()

    override fun initializeViews() {
        super.initializeViews()
        binding.fragment = this
    }

    override val progressBar: ProgressBar
        get() = binding.progressBar

    fun continueAnonymously() {
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