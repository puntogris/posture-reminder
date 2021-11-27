package com.puntogris.posture.ui.login

import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentInternalLoginBinding
import com.puntogris.posture.ui.base.BaseLoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InternalLoginFragment :
    BaseLoginFragment<FragmentInternalLoginBinding>(R.layout.fragment_internal_login) {

    override val viewModel: LoginViewModel by viewModels()

    override val progressBar: ProgressBar
        get() = binding.progressBar

    override fun initializeViews() {
        super.initializeViews()
        binding.fragment = this
    }

    fun onNavigateUp() {
        findNavController().navigateUp()
    }
}