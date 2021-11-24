package com.puntogris.posture.ui.welcome

import androidx.lifecycle.lifecycleScope
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.databinding.FragmentWelcomeBinding
import com.puntogris.posture.ui.base.BaseBindingFragment
import com.puntogris.posture.utils.isDarkThemeOn
import com.puntogris.posture.utils.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : BaseBindingFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    @Inject
    lateinit var dataStore: DataStore

    override fun initializeViews() {
        with(binding) {
            fragment = this@WelcomeFragment
            viewPager.adapter = WelcomePagerAdapter(isDarkThemeOn())
            dotsIndicator.setViewPager2(viewPager)
        }
    }

    fun onStartButtonClicked() {
        lifecycleScope.launch {
            dataStore.setShowWelcomePref(false)
            navigateTo(R.id.action_welcome_to_batteryOptimization)
        }
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}