package com.puntogris.posture.ui.base

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.NavigationDirections
import com.puntogris.posture.R
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.UiInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseLoginFragment<T : ViewDataBinding>(@LayoutRes override val layout: Int) :
    BaseBindingFragment<T>(layout), LoginConfiguration {

    private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun initializeViews() {
        super.initializeViews()
        registerActivityResultLauncher()
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
}