package com.puntogris.posture.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentBatteryOptimizationBinding
import com.puntogris.posture.utils.constants.Constants.SETTINGS_FLOW
import com.puntogris.posture.utils.constants.Constants.WELCOME_FLOW
import com.puntogris.posture.utils.extensions.isDarkThemeOn
import com.puntogris.posture.utils.extensions.isIgnoringBatteryOptimizations
import com.puntogris.posture.utils.viewBinding

class BatteryOptimizationFragment : Fragment(R.layout.fragment_battery_optimization) {

    private val binding by viewBinding(FragmentBatteryOptimizationBinding::bind)
    private val args: BatteryOptimizationFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setBatteryOptimizationsStepsUi()
    }

    private fun setupListeners() {
        binding.buttonSkip.setOnClickListener {
            navigateAccordingToFlow()
        }
        binding.buttonContinue.setOnClickListener {
            if (requireContext().isIgnoringBatteryOptimizations()) {
                navigateAccordingToFlow()
            } else {
                startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
            }
        }
    }

    private fun navigateAccordingToFlow() {
        when(args.flow) {
            WELCOME_FLOW -> {
                val action = BatteryOptimizationFragmentDirections.actionGlobalPermissionsFragment(args.flow)
                findNavController().navigate(action)
            }
            SETTINGS_FLOW -> {
                findNavController().navigateUp()
            }
            else -> {
                findNavController().navigate(R.id.homeFragment)
            }
        }
    }

    private fun setBatteryOptimizationsStepsUi() {
        val (stepOne, stepTwo) = if (isDarkThemeOn()) {
            R.string.battery_optimization_step_one_light to R.string.battery_optimization_step_two_light
        } else {
            R.string.battery_optimization_step_one_dark to R.string.battery_optimization_step_two_dark
        }
        binding.textViewStepOne.text = htmlToString(stepOne)
        binding.textViewStepTwo.text = htmlToString(stepTwo)
    }

    private fun htmlToString(htmlRes: Int) = HtmlCompat.fromHtml(
        getString(htmlRes),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}

