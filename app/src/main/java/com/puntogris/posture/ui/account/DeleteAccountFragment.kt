package com.puntogris.posture.ui.account

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentDeleteAccountBinding
import com.puntogris.posture.ui.base.BaseBindingFragment
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.UiInterface
import com.puntogris.posture.utils.playAnimationOnce
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
        binding.animationView.playAnimation()

        lifecycleScope.launch {
            val user = viewModel.getCurrentUser()
            if (user != null && user.email == binding.emailField.text.toString()) {
                deleteAccount()
            } else {
                UiInterface.showSnackBar("The email doesn't match the one in your account.")
            }
        }
    }

    private suspend fun deleteAccount() {
        lifecycleScope.launch {
            when (viewModel.deleteAccount()) {
                SimpleResult.Failure -> onDeleteFailure()
                SimpleResult.Success -> onDeleteSuccess()
            }
        }
    }

    private fun onDeleteFailure() {
        binding.animationView.playAnimationOnce(R.raw.error)
        UiInterface.showSnackBar(getString(R.string.snack_connection_error))
    }

    private fun onDeleteSuccess() {
        UiInterface.showSnackBar(getString(R.string.snack_account_deleted_success))
        binding.animationView.playAnimationOnce(R.raw.success)
        binding.deleteButton.setOnClickListener {
            val nav = NavOptions.Builder().setPopUpTo(R.id.navigation, true).build()
            findNavController().navigate(R.id.loginFragment, null, nav)
        }
    }
}