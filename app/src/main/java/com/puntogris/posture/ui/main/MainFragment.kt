package com.puntogris.posture.ui.main

import androidx.fragment.app.activityViewModels
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentMainBinding
import com.puntogris.posture.ui.MainViewModel
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.*
import dagger.hilt.android.AndroidEntryPoint

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

    fun onToggleReminderClicked(){
//        if (viewModel.isAppActive()) {
//            viewModel.cancelAlarms()
//            createSnackBar(getString(R.string.alarms_off_toast))
//        }
//        else {
//            viewModel.startAlarm()
//            createSnackBar(getString(
//                R.string.notifications_set_toast,
//                minutesFromMidnightToHourlyTime(viewModel.reminder.value!!.startTime),
//                minutesFromMidnightToHourlyTime(viewModel.reminder.value!!.endTime)
//            ))
//        }
      //  binding.enableSummaryTextview.playShakeAnimation()
     //   binding.enableTextView.playShakeAnimation()
    }

    override fun onDestroyView() {
        binding.pager.adapter = null
        super.onDestroyView()
    }
}