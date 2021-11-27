package com.puntogris.posture.feature_main.presentation.welcome

import android.content.Intent
import android.provider.Settings
import androidx.core.text.HtmlCompat
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentBatteryOptimizationBinding
import com.puntogris.posture.common.presentation.base.BaseBindingFragment
import com.puntogris.posture.common.utils.gone
import com.puntogris.posture.common.utils.isDarkThemeOn
import com.puntogris.posture.common.utils.isIgnoringBatteryOptimizations
import com.puntogris.posture.common.utils.navigateTo

class BatteryOptimizationFragment :
    BaseBindingFragment<FragmentBatteryOptimizationBinding>(R.layout.fragment_battery_optimization) {

    override fun initializeViews() {
        binding.fragment = this
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

    fun openBatteryOptimization() {
        startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
    }

    fun onFinalizeButtonClicked() {
        navigateTo(R.id.action_batteryOptimization_to_home)
    }

    override fun onResume() {
        checkPowerStatus()
        super.onResume()
    }
}