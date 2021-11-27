package com.puntogris.posture.feature_main.presentation.account

import androidx.fragment.app.viewModels
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentAccountBinding
import com.puntogris.posture.common.presentation.base.BaseFragmentOptions
import com.puntogris.posture.common.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : BaseFragmentOptions<FragmentAccountBinding>(R.layout.fragment_account) {

    private val viewModel: AccountViewModel by viewModels()

    override fun initializeViews() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    fun onNavigateToRemindersClicked() {
        navigateTo(R.id.manageRemindersBottomSheet)
    }
}