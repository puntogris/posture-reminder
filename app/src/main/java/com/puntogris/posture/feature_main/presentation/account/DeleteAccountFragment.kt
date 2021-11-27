package com.puntogris.posture.feature_main.presentation.account

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentDeleteAccountBinding
import com.puntogris.posture.common.presentation.base.BaseBindingFragment
import com.puntogris.posture.common.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeleteAccountFragment :
    BaseBindingFragment<FragmentDeleteAccountBinding>(R.layout.fragment_delete_account) {

    private val viewModel: DeleteAccountViewModel by viewModels()

    override fun initializeViews() {
        binding.fragment = this
    }

    fun onDeleteAccountClicked() {
        lifecycleScope.launch {
            val user = viewModel.getCurrentUser()
            if (
                user != null &&
                user.email.isNotBlank() &&
                user.email == binding.emailField.text.toString()
            ) {
                showLoadingProgress()
                deleteAccount()
            } else {
                UiInterface.showSnackBar(getString(R.string.snack_account_email_not_matching))
            }
        }
    }

    private fun showLoadingProgress() {
        with(binding) {
            deleteAccountAnimation.visible()
            deleteAccountAnimation.setAnimation(R.raw.loading)
            deleteAccountAnimation.repeatCount = LottieDrawable.INFINITE
            deleteAccountAnimation.playAnimation()
            emailField.gone()
        }
    }

    private suspend fun deleteAccount() {
        when (viewModel.deleteAccount()) {
            SimpleResult.Failure -> onDeleteFailure()
            SimpleResult.Success -> onDeleteSuccess()
        }
    }

    private fun onDeleteFailure() {
        with(binding) {
            deleteAccountAnimation.gone()
            deleteAccountAnimation.playAnimationOnce(R.raw.error)
            emailField.visible()
        }
        UiInterface.showSnackBar(getString(R.string.snack_connection_error))
    }

    private fun onDeleteSuccess() {
        UiInterface.showSnackBar(getString(R.string.snack_account_deleted_success))
        val nav = NavOptions.Builder().setPopUpTo(R.id.navigation, true).build()
        findNavController().navigate(R.id.loginFragment, null, nav)
    }
}