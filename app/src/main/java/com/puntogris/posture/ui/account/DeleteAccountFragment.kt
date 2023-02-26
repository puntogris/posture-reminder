package com.puntogris.posture.ui.account

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentDeleteAccountBinding
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.extensions.*
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeleteAccountFragment : Fragment(R.layout.fragment_delete_account) {

    private val viewModel: DeleteAccountViewModel by viewModels()
    private val binding by viewBinding(FragmentDeleteAccountBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonDeleteAccount.setOnClickListener {
            onDeleteAccountClicked()
        }
    }

    private fun onDeleteAccountClicked() {
        hideKeyBoard()
        lifecycleScope.launch {
            if (viewModel.getCurrentUserEmail() == binding.editTextAccountEmail.text.toString()) {
                showLoadingProgress()
                deleteAccount()
            } else {
                showSnackBar(R.string.snack_account_email_not_matching)
            }
        }
    }

    private fun showLoadingProgress() {
        binding.deleteAccountAnimation.apply {
            isVisible = true
            setAnimation(R.raw.loading)
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
        binding.editTextAccountEmail.isVisible = false
    }

    private suspend fun deleteAccount() {
        when (viewModel.deleteAccount()) {
            SimpleResult.Failure -> onDeleteFailure()
            SimpleResult.Success -> onDeleteSuccess()
        }
    }

    private fun onDeleteFailure() {
        with(binding) {
            deleteAccountAnimation.isVisible = false
            deleteAccountAnimation.playAnimationOnce(R.raw.error)
            editTextAccountEmail.isVisible = true
        }
        showSnackBar(R.string.snack_connection_error)
    }

    private fun onDeleteSuccess() {
        UiInterface.showSnackBar(getString(R.string.snack_account_deleted_success))
        val nav = NavOptions.Builder().setPopUpTo(R.id.navigation, true).build()
        findNavController().navigate(R.id.loginFragment, null, nav)
    }
}
