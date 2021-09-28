package com.puntogris.posture.ui.welcome

import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentWelcomeBinding
import com.puntogris.posture.ui.base.BaseFragment
import com.puntogris.posture.utils.isDarkThemeOn
import com.puntogris.posture.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment: BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    override fun initializeViews() {
        with(binding){
            fragment = this@WelcomeFragment
            viewPager.adapter = WelcomePagerAdapter(isDarkThemeOn())
            dotsIndicator.setViewPager2(viewPager)
        }
    }

    fun onStartButtonClicked(){
        navigateTo(R.id.action_welcomeFragment_to_batteryOptimizationFragment)
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}