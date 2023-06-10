package com.puntogris.posture.ui.sync

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentSyncAccountBinding
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.WELCOME_FLOW
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.playAnimationOnce
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SyncAccountFragment : Fragment(R.layout.fragment_sync_account) {

    private val viewModel: SyncAccountViewModel by viewModels()
    private val binding by viewBinding(FragmentSyncAccountBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()

        binding.buttonContinue.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.showWelcome()) {
                    val action =
                        SyncAccountFragmentDirections.actionGlobalBatteryOptimizationFragment(
                            WELCOME_FLOW
                        )
                    findNavController().navigate(action)
                } else {
                    findNavController().navigate(R.id.action_syncAccount_to_home)
                }
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
        with(binding) {
            animationSync.playAnimationOnce(R.raw.error)
            textViewSyncTitle.setText(R.string.account_sync_error)

            with(buttonContinue) {
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
        with(binding) {
            animationSync.playAnimationOnce(R.raw.success)
            textViewSyncTitle.setText(R.string.account_sync_success)
            buttonContinue.isEnabled = true
        }
    }
}
