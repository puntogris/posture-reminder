package com.puntogris.posture.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentBatteryOptimizationBinding
import com.puntogris.posture.utils.extensions.gone
import com.puntogris.posture.utils.extensions.isDarkThemeOn
import com.puntogris.posture.utils.extensions.isIgnoringBatteryOptimizations
import com.puntogris.posture.utils.extensions.navigateTo
import com.puntogris.posture.utils.viewBinding

class BatteryOptimizationFragment : Fragment(R.layout.fragment_battery_optimization) {

    private val binding by viewBinding(FragmentBatteryOptimizationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.finishOptimizationButton.setOnClickListener {
            navigateTo(R.id.action_batteryOptimization_to_home)
        }
        binding.disableOptimizationButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        }
        checkPowerStatus()
        setBatteryOptimizationsStepsUi()
    }

    private fun setBatteryOptimizationsStepsUi() {
        val stepOne =
            if (isDarkThemeOn()) R.string.battery_optimization_step_one_light
            else R.string.battery_optimization_step_one_dark

        val stepTwo =
            if (isDarkThemeOn()) R.string.battery_optimization_step_two_light
            else R.string.battery_optimization_step_two_dark

        binding.stepOne.text = htmlToString(stepOne)
        binding.stepTwo.text = htmlToString(stepTwo)
    }

    private fun htmlToString(htmlRes: Int) =
        HtmlCompat.fromHtml(getString(htmlRes), HtmlCompat.FROM_HTML_MODE_LEGACY)

    private fun checkPowerStatus() {
        if (requireContext().isIgnoringBatteryOptimizations()) {
            binding.apply {
                powerManagerState.setText(R.string.optimization_correct)
                powerStateImage.setImageResource(R.drawable.ic_baseline_check_circle_24)
                requireOptimizationGroup.gone()
            }
        }
    }

    override fun onResume() {
        checkPowerStatus()
        super.onResume()
    }
}