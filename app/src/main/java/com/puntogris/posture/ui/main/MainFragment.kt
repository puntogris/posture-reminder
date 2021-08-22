package com.puntogris.posture.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentMainBinding
import com.puntogris.posture.model.AlarmStatus
import com.puntogris.posture.ui.MainViewModel
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.Constants.PACKAGE_URI_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment: BaseFragmentOptions<FragmentMainBinding>(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels()

    private val requestPermissionLauncher: ActivityResultLauncher<Intent> by lazy {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) viewModel.toggleAlarm()
            else UiInterface.showSnackBar(getString(R.string.snack_permission_required))
        }
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) showSnackWithPermissionAction()
        else viewModel.toggleAlarm()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showSnackWithPermissionAction(){
        UiInterface.showSnackBar(
            message = getString(R.string.snack_action_require_permission),
            actionText = R.string.action_give_permission)
        {
            startAlarmPermissionIntent()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startAlarmPermissionIntent(){
        requestPermissionLauncher.launch(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also {
            it.data = Uri.parse(PACKAGE_URI_NAME)
        })
    }

    override fun onDestroyView() {
        binding.pager.adapter = null
        super.onDestroyView()
    }
}