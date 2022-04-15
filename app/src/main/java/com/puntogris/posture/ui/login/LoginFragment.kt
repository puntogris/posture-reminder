package com.puntogris.posture.ui.login

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentLoginBinding
import com.puntogris.posture.ui.base.BaseLoginFragment
import com.puntogris.posture.utils.extensions.launchWebBrowserIntent
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseLoginFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override val viewModel: LoginViewModel by viewModels()
    override val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    override val progressBar: ProgressBar
        get() = binding.progressBar
    override val loginWithGoogleButton: View
        get() = binding.loginWithGoogleButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueAnonymouslyButton.setOnClickListener {
            val action = LoginFragmentDirections.actionGlobalSynAccountFragment()
            findNavController().navigate(action)
        }
        binding.loginProblemsButton.setOnClickListener {
            launchWebBrowserIntent("https://postureapp.puntogris.com/help/")
        }
    }
}