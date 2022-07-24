package com.puntogris.posture.ui.sync

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentSynAccountBinding
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.navigateTo
import com.puntogris.posture.utils.extensions.playAnimationOnce
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SynAccountFragment : Fragment(R.layout.fragment_syn_account) {

    private val viewModel: SyncAccountViewModel by viewModels()
    private val binding by viewBinding(FragmentSynAccountBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()

        binding.syncContinueButton.setOnClickListener {
            lifecycleScope.launch {
                navigateTo(
                    if (viewModel.showWelcome()) {
                        R.id.action_synAccountFragment_to_welcomeFragment
                    } else {
                        R.id.action_synAccount_to_home
                    }
                )
            }
        }
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
}
