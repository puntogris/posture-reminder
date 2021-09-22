package com.puntogris.posture.ui.welcome

import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentWelcomeBinding
import com.puntogris.posture.ui.base.BaseFragment
import com.puntogris.posture.utils.isDarkThemeOn
import com.puntogris.posture.utils.navigateTo

class WelcomeFragment :BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    override fun initializeViews() {
        binding.fragment = this
        binding.viewPager.adapter = WelcomePagerAdapter(isDarkThemeOn())
        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }

    fun onStartButtonClicked(){
        navigateTo(R.id.action_welcomeFragment_to_batteryOptimizationFragment)
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}