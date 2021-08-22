package com.puntogris.posture.ui.main

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentMainBinding
import com.puntogris.posture.model.AlarmStatus
import com.puntogris.posture.ui.MainViewModel
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment: BaseFragmentOptions<FragmentMainBinding>(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun initializeViews() {
        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.fragment = this
            it.pandaAnimation.setPadding(0, 0, -100, -110)
        }
        setupPager()
        observeCurrentReminderState()
    }

    private fun setupPager(){
        val pagerAdapter = DayHistoryMainPagerAdapter()
        subscribeUi(pagerAdapter)
        binding.pager.apply {
            adapter = pagerAdapter
            binding.dotsIndicator.setViewPager2(this)
            setPageFadeTransformer()
        }
    }

    private fun subscribeUi(adapter: DayHistoryMainPagerAdapter){
        viewModel.getLastTwoDaysHistory().observe(viewLifecycleOwner){
            adapter.updateList(it)
        }
    }

    private fun observeCurrentReminderState(){
        lifecycleScope.launch {
            viewModel.alarmStatus.collect {
                handleAlarmStatusResult(it)
            }
        }
    }

    private fun handleAlarmStatusResult(result: AlarmStatus){
        when(result){
            is AlarmStatus.Activated -> {
                UiInterface.showSnackBar(
                    getString(R.string.snack_notifications_set,
                        Utils.minutesFromMidnightToHourlyTime(result.reminder.startTime),
                        Utils.minutesFromMidnightToHourlyTime(result.reminder.endTime)
                    ))
            }
            AlarmStatus.Canceled -> {
                UiInterface.showSnackBar(getString(R.string.snack_alarms_off))
            }
            AlarmStatus.NoConfigured -> {
                UiInterface.showSnackBar(getString(R.string.snack_active_reminder_not_found))
            }
        }
    }

    fun onToggleReminderClicked(){
        lifecycleScope.launch {
            viewModel.toggleAlarm()
        }
    }

    override fun onDestroyView() {
        binding.pager.adapter = null
        super.onDestroyView()
    }
}