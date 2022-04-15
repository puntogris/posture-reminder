package com.puntogris.posture.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.puntogris.posture.NavigationDirections
import com.puntogris.posture.R
import com.puntogris.posture.domain.model.LoginResult
import com.puntogris.posture.ui.login.LoginConfiguration
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.gone
import com.puntogris.posture.utils.extensions.visible
import kotlinx.coroutines.launch

abstract class BaseLoginFragment<T : ViewBinding>(layoutId: Int) : Fragment(layoutId),
    LoginConfiguration {

    protected abstract val binding: T
    private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerActivityResultLauncher()
        loginWithGoogleButton.setOnClickListener {
            startLoginWithGoogle()
        }

    }

    private fun registerActivityResultLauncher() {
        loginActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                onLoginFinished()
                authGoogleUserIntoServer(it)
            }
    }

    private fun authGoogleUserIntoServer(result: ActivityResult) {
        lifecycleScope.launch {
            viewModel.authGoogleUser(result).collect(::handleAuthUserIntoServerResult)
        }
    }

    private fun handleAuthUserIntoServerResult(result: LoginResult) {
        when (result) {
            is LoginResult.Error -> {
                UiInterface.showSnackBar(
                    getString(R.string.snack_fail_login),
                    anchorToBottomNav = false
                )
                onLoginError()
            }
            LoginResult.InProgress -> {
                onLoginStarted()
            }
            is LoginResult.Success -> {
                val action =
                    NavigationDirections.actionGlobalSynAccountFragment(result.userPrivateData)
                findNavController().navigate(action)
            }
        }
    }

    fun startLoginWithGoogle() {
        onLoginStarted()
        val intent = viewModel.getGoogleSignInIntent()
        loginActivityResultLauncher.launch(intent)
    }

    override fun onLoginError() {
        progressBar.gone()
    }

    override fun onLoginFinished() {
        progressBar.gone()
    }

    override fun onLoginStarted() {
        progressBar.visible()
    }
}