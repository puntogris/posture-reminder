package com.puntogris.posture.ui.welcome

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.databinding.FragmentWelcomeBinding
import com.puntogris.posture.utils.extensions.isDarkThemeOn
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.navigateTo
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    @Inject
    lateinit var dataStore: DataStore
    private val binding by viewBinding(FragmentWelcomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewPager.adapter = WelcomePagerAdapter(isDarkThemeOn())
            dotsIndicator.setViewPager2(viewPager)
            welcomeContinueButton.setOnClickListener {
                lifecycleScope.launch {
                    dataStore.setShowWelcomePref(false)
                    navigateTo(R.id.action_welcome_to_batteryOptimization)
                }
            }
        }
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}
