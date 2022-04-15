package com.puntogris.posture.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentInternalLoginBinding
import com.puntogris.posture.databinding.FragmentLoginBinding
import com.puntogris.posture.ui.base.BaseLoginFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InternalLoginFragment : BaseLoginFragment<FragmentInternalLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()
    override val binding by viewBinding(FragmentInternalLoginBinding::bind)
    override val progressBar: ProgressBar = binding.progressBar
    override val loginWithGoogleButton: View = binding.loginWithGoogleButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueAnonymouslyButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}