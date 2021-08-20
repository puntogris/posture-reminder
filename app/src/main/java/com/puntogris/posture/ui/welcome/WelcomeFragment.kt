package com.puntogris.posture.ui.welcome

import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentWelcomeBinding
import com.puntogris.posture.ui.base.BaseFragment

class WelcomeFragment :BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    override fun initializeViews() {
        binding.fragment = this
        binding.viewPager.adapter = WelcomePagerAdapter()
        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }

    fun onStartButtonClicked(){
        findNavController().navigate(R.id.action_welcomeFragment_to_batteryOptimizationFragment)
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}