package com.puntogris.posture.ui.base

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.puntogris.posture.NavigationDirections
import com.puntogris.posture.R
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.UiInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseLoginFragment<T: ViewDataBinding>(@LayoutRes override val layout: Int):
    BaseFragment<T>(layout), LoginConfiguration {

    private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun initializeViews() {
        super.initializeViews()
        registerActivityResultLauncher()
    }

    private fun registerActivityResultLauncher(){
        loginActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            onLoginFinished()
            if (it.resultCode == Activity.RESULT_OK)
                handleLoginActivityResult(it.data)
            else if (it.resultCode == Activity.RESULT_CANCELED) {
                UiInterface.showSnackBar(getString(R.string.snack_fail_login), anchorToBottomNav = false)
            }
        }
    }

    private fun handleLoginActivityResult(intent: Intent?){
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(ApiException::class.java)!!
            authUserIntoFirebase(account.idToken!!)
        } catch (e: ApiException) {
            UiInterface.showSnackBar(getString(R.string.snack_fail_login), anchorToBottomNav = false)
        }
    }

    private fun authUserIntoFirebase(idToken: String){
        lifecycleScope.launch {
            viewModel.authUserWithFirebase(idToken).collect {
                handleAuthUserIntoFirebaseResult(it)
            }
        }
    }

    fun startLoginWithGoogle() {
        onLoginStarted()
        val intent = viewModel.getGoogleSignInIntent()
        loginActivityResultLauncher.launch(intent)
    }

    private fun handleAuthUserIntoFirebaseResult(result: LoginResult){
        when (result) {
            is LoginResult.Error -> {
                UiInterface.showSnackBar(getString(R.string.snack_fail_login), anchorToBottomNav = false)
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
}