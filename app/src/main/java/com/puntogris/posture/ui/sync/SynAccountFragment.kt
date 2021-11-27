package com.puntogris.posture.ui.sync

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentSynAccountBinding
import com.puntogris.posture.ui.base.BaseBindingFragment
import com.puntogris.posture.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SynAccountFragment :
    BaseBindingFragment<FragmentSynAccountBinding>(R.layout.fragment_syn_account) {

    private val viewModel: SyncAccountViewModel by viewModels()

    override fun initializeViews() {
        binding.fragment = this
        subscribeUi()
    }

    private fun subscribeUi() {
        launchAndRepeatWithViewLifecycle(Lifecycle.State.CREATED) {
            viewModel.syncStatus.collect {
                when (it) {
                    SimpleResult.Failure -> onSyncAccountFailure()
                    SimpleResult.Success -> onSyncAccountSuccess()
                }
            }
        }
    }

    private fun onSyncAccountFailure() {
        binding.apply {
            syncAnimation.playAnimationOnce(R.raw.error)
            syncTitle.setText(R.string.account_sync_error)

            with(syncContinueButton) {
                setText(R.string.action_exit)
                isEnabled = true
                setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.logOut()
                        findNavController().navigateUp()
                        UiInterface.showSnackBar(getString(R.string.snack_connection_error))
                    }
                }
            }
        }
    }

    private fun onSyncAccountSuccess() {
        binding.apply {
            syncAnimation.playAnimationOnce(R.raw.success)
            syncTitle.setText(R.string.account_sync_success)
            syncContinueButton.isEnabled = true
        }
    }

    fun onContinueButtonClicked() {
        lifecycleScope.launch {
            navigateTo(
                if (viewModel.showWelcome()) R.id.action_synAccountFragment_to_welcomeFragment
                else R.id.action_synAccount_to_home
            )
        }
    }
}