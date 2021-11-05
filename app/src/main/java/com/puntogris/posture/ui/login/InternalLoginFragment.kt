package com.puntogris.posture.ui.login

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentInternalLoginBinding
import com.puntogris.posture.ui.base.BaseLoginFragment
import com.puntogris.posture.utils.gone
import com.puntogris.posture.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InternalLoginFragment :
    BaseLoginFragment<FragmentInternalLoginBinding>(R.layout.fragment_internal_login) {

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

    fun onNavigateUp() {
        findNavController().navigateUp()
    }

}