package com.puntogris.posture.feature_auth.presentation

import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentLoginBinding
import com.puntogris.posture.common.presentation.base.BaseLoginFragment
import com.puntogris.posture.common.utils.launchWebBrowserIntent
import dagger.hilt.android.AndroidEntryPoint

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
        val action = LoginFragmentDirections.actionGlobalSynAccountFragment()
        findNavController().navigate(action)
    }

    fun onLoginProblemsClicked() {
        launchWebBrowserIntent("https://postureapp.puntogris.com/help/")
    }
}