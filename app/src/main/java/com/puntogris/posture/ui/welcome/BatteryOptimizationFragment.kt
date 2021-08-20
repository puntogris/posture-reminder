package com.puntogris.posture.ui.welcome

import androidx.navigation.fragment.findNavController
import com.puntogris.posture.databinding.FragmentBatteryOptimizationBinding
import com.puntogris.posture.ui.base.BaseFragment
import android.content.Intent
import android.provider.Settings
import com.puntogris.posture.utils.gone
import androidx.core.text.HtmlCompat
import com.puntogris.posture.R
import com.puntogris.posture.utils.isDarkThemeOn
import com.puntogris.posture.utils.isIgnoringBatteryOptimizations

class BatteryOptimizationFragment : BaseFragment<FragmentBatteryOptimizationBinding>(R.layout.fragment_battery_optimization) {

    override fun initializeViews() {
        binding.fragment = this
        checkPowerStatus()
        setBatteryOptimizationsStepsUi()
    }

    private fun setBatteryOptimizationsStepsUi(){
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

    private fun checkPowerStatus(){
        if (isIgnoringBatteryOptimizations()){
            binding.apply {
                powerManagerState.text = "Todo en orden"
                powerStateImage.setImageResource(R.drawable.ic_baseline_check_circle_24)
                requireOptimizationGroup.gone()
            }
        }
    }

    fun openBatteryOptimization(){
        startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
    }

    fun onFinalizeButtonClicked(){
        findNavController().navigate(R.id.action_batteryOptimizationFragment_to_mainFragment)
    }

    override fun onResume() {
        checkPowerStatus()
        super.onResume()
    }
}