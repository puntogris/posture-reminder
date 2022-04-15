package com.puntogris.posture.ui.login

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentInternalLoginBinding
import com.puntogris.posture.ui.base.BaseLoginFragment
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InternalLoginFragment :
    BaseLoginFragment<FragmentInternalLoginBinding>(R.layout.fragment_internal_login) {

    override val binding by viewBinding(FragmentInternalLoginBinding::bind)
    override val viewModel: LoginViewModel by viewModels()
    override val progressBar: ProgressBar
        get() = binding.progressBar
    override val loginWithGoogleButton: View
        get() = binding.loginWithGoogleButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueAnonymouslyButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}