package com.puntogris.posture.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentHomeBinding
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.domain.repository.SyncRepository
import com.puntogris.posture.framework.alarm.AlarmStatus
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.UiInterface
import com.puntogris.posture.utils.Utils
import com.puntogris.posture.utils.constants.Constants.PACKAGE_URI_NAME
import com.puntogris.posture.utils.navigateTo
import com.puntogris.posture.utils.setPageFadeTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragmentOptions<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private var mediator: TabLayoutMediator? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Intent>

    override fun initializeViews() {
        binding.let {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            it.fragment = this
            it.pandaAnimation.setPadding(0, 0, -100, -110)
        }

        setupPagerAndTabLayout()
        observeAlarmStatus()
        registerAlarmPermissionLauncher()

    }

    private fun setupPagerAndTabLayout() {
        val pagerAdapter = DayLogHomeAdapter()
        subscribePager(pagerAdapter)

        binding.usagePager.pager.apply {
            adapter = pagerAdapter
            setPageFadeTransformer()
        }
        mediator =
            TabLayoutMediator(binding.usagePager.tabLayout, binding.usagePager.pager) { _, _ -> }
        mediator?.attach()
    }

    private fun subscribePager(adapter: DayLogHomeAdapter) {
        viewModel.getLastTwoDaysHistory.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }

    private fun observeAlarmStatus() {
        lifecycleScope.launch {
            viewModel.alarmStatus.collect(::handleAlarmStatusResult)
        }
    }

    private fun handleAlarmStatusResult(result: AlarmStatus) {
        when (result) {
            is AlarmStatus.Activated -> {
                UiInterface.showSnackBar(
                    getString(
                        R.string.snack_notifications_set,
                        Utils.minutesFromMidnightToHourlyTime(result.reminder.startTime),
                        Utils.minutesFromMidnightToHourlyTime(result.reminder.endTime)
                    )
                )
            }
            AlarmStatus.Canceled -> {
                UiInterface.showSnackBar(getString(R.string.snack_alarms_off))
            }
            AlarmStatus.NotConfigured -> {
                UiInterface.showSnackBar(
                    getString(R.string.snack_active_reminder_not_found),
                    actionText = R.string.action_select
                ) {
                    navigateTo(R.id.manageRemindersBottomSheet)
                }
            }
        }
    }

    private fun registerAlarmPermissionLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) viewModel.toggleAlarm()
                    else UiInterface.showSnackBar(getString(R.string.snack_permission_required))
                }
        }
    }

    fun onToggleAlarmClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !viewModel.canScheduleExactAlarms()) {
            showSnackWithPermissionAction()
        } else {
            viewModel.toggleAlarm()
        }
    }

    fun onNavigateToRemindersClicked() {
        navigateTo(R.id.manageRemindersBottomSheet)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showSnackWithPermissionAction() {
        UiInterface.showSnackBar(
            message = getString(R.string.snack_action_require_permission),
            actionText = R.string.action_give_permission
        )
        {
            startAlarmPermissionIntent()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startAlarmPermissionIntent() {
        requestPermissionLauncher.launch(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).also {
            it.data = Uri.parse(PACKAGE_URI_NAME)
        })
    }

    fun onNavigateToEditReminder(reminder: Reminder) {
        val action = HomeFragmentDirections.actionHomeToNewReminder(reminder)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        mediator?.detach()
        mediator = null
        binding.usagePager.pager.adapter = null
        super.onDestroyView()
    }
}