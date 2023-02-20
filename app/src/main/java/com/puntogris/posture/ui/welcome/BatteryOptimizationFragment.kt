package com.puntogris.posture.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentBatteryOptimizationBinding
import com.puntogris.posture.utils.extensions.isDarkThemeOn
import com.puntogris.posture.utils.extensions.isIgnoringBatteryOptimizations
import com.puntogris.posture.utils.viewBinding

class BatteryOptimizationFragment : Fragment(R.layout.fragment_battery_optimization) {

    private val binding by viewBinding(FragmentBatteryOptimizationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setBatteryOptimizationsStepsUi()
    }

    private fun setupListeners() {
        binding.buttonSkip.setOnClickListener {
            findNavController().navigate(R.id.action_batteryOptimization_to_home)
        }
        binding.buttonContinue.setOnClickListener {
            if (requireContext().isIgnoringBatteryOptimizations()) {
                findNavController().navigate(R.id.action_batteryOptimization_to_home)
            } else {
                startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
            }
        }
    }

    private fun setBatteryOptimizationsStepsUi() {
        val (stepOne, stepTwo) = if (isDarkThemeOn()) {
            R.string.battery_optimization_step_one_light to R.string.battery_optimization_step_two_light
        } else {
            R.string.battery_optimization_step_one_dark to R.string.battery_optimization_step_two_dark
        }
        binding.stepOne.text = htmlToString(stepOne)
        binding.stepTwo.text = htmlToString(stepTwo)
    }

    private fun htmlToString(htmlRes: Int) = HtmlCompat.fromHtml(
        getString(htmlRes),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )

    override fun onResume() {
        if (requireContext().isIgnoringBatteryOptimizations()) {
            findNavController().navigate(R.id.action_batteryOptimization_to_home)
        }
        super.onResume()
    }
}