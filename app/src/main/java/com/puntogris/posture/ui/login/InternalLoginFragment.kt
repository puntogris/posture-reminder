package com.puntogris.posture.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.NavigationDirections
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentInternalLoginBinding
import com.puntogris.posture.domain.model.LoginResult
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.setIntentLauncher
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InternalLoginFragment : Fragment(R.layout.fragment_internal_login) {

    private val binding by viewBinding(FragmentInternalLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginLauncher = setIntentLauncher {
            lifecycleScope.launch {
                viewModel.authGoogleUser(it).collectLatest(::handleAuthUserResult)
            }
        }

        binding.loginWithGoogleButton.setOnClickListener {
            loginLauncher.launch(viewModel.getGoogleSignInIntent())
        }
    }

    private fun handleAuthUserResult(result: LoginResult) {
        when (result) {
            is LoginResult.Error -> {
                UiInterface.showSnackBar(
                    getString(R.string.snack_fail_login),
                    anchorToBottomNav = false
                )
                binding.progressBar.isVisible = false
            }
            LoginResult.InProgress -> {
                binding.progressBar.isVisible = true
            }
            is LoginResult.Success -> {
                val action = NavigationDirections.actionGlobalSynAccountFragment(
                    result.userPrivateData
                )
                findNavController().navigate(action)
            }
        }
    }
}
