package com.puntogris.posture.ui.sync

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentSynAccountBinding
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.ui.base.BaseFragment
import com.puntogris.posture.utils.playAnimationOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SynAccountFragment : BaseFragment<FragmentSynAccountBinding>(R.layout.fragment_syn_account) {

    private val viewModel: SyncAccountViewModel by viewModels()
    private val args: SynAccountFragmentArgs by navArgs()

    override fun initializeViews() {
        binding.fragment = this
        startAccountSync()
    }

    private fun startAccountSync(){
        lifecycleScope.launch {
            when(viewModel.synAccountWith(args.userPrivateData)){
                SimpleResult.Failure -> onSyncAccountFailure()
                SimpleResult.Success -> onSyncAccountSuccess()
            }
        }
    }

    private fun onSyncAccountSuccess(){
        binding.apply {
            animationView.playAnimationOnce(R.raw.success)
            continueButton.isEnabled = true
            title.text = getString(R.string.account_sync_success)
        }
    }

    private fun onSyncAccountFailure(){
        binding.apply {
            animationView.playAnimationOnce(R.raw.error)
            title.text = getString(R.string.account_sync_error)
        }
    }

    fun onContinueButtonClicked(){
        findNavController().navigate(R.id.action_synAccountFragment_to_welcomeFragment)
    }

}