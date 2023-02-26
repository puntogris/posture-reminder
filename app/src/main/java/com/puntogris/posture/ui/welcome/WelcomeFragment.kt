package com.puntogris.posture.ui.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.databinding.FragmentWelcomeBinding
import com.puntogris.posture.utils.constants.Constants.WELCOME_FLOW
import com.puntogris.posture.utils.extensions.isDarkThemeOn
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    @Inject
    lateinit var dataStoreHelper: DataStoreHelper
    private val binding by viewBinding(FragmentWelcomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            viewPager.adapter = WelcomePagerAdapter(isDarkThemeOn())
            dotsIndicator.setViewPager2(viewPager)
            buttonContinue.setOnClickListener {
                onContinueButtonClicked()
            }
        }
    }

    private fun onContinueButtonClicked() {
        lifecycleScope.launch {
            dataStoreHelper.setShowWelcomePref(false)
            val action = WelcomeFragmentDirections.actionGlobalBatteryOptimizationFragment(
                WELCOME_FLOW
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}
