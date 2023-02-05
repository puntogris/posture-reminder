package com.puntogris.posture.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentHomeBinding
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.framework.alarm.AlarmStatus
import com.puntogris.posture.utils.Utils
import com.puntogris.posture.utils.constants.Constants.PACKAGE_URI_NAME
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.setPageFadeTransformer
import com.puntogris.posture.utils.extensions.showItem
import com.puntogris.posture.utils.setBackgroundColorTintView
import com.puntogris.posture.utils.setDaysSummary
import com.puntogris.posture.utils.setMinutesToHourlyTime
import com.puntogris.posture.utils.setToggleButton
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private var mediator: TabLayoutMediator? = null
    private lateinit var pagerAdapter: DayLogHomeAdapter
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pandaAnimation.setPadding(0, 0, -100, -110)
        setupListeners()
        setupObservers()
        setupPagerAndTabLayout()
        registerAlarmPermissionLauncher()
    }

    private fun setupObservers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.isAlarmActive.collectLatest {
                binding.activeReminderLayout.toggleReminderButton.setToggleButton(it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.isPandaAnimationEnabled.collectLatest {
                binding.pandaAnimation.isVisible = it
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.alarmStatus.collectLatest(::handleAlarmStatusResult)
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.getLastTwoDaysHistory.collectLatest(pagerAdapter::updateList)
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.activeReminder.collectLatest(::onActiveReminderUpdated)
        }
    }

    private fun setupListeners() {
        binding.activeReminderLayout.selectReminderButton.setOnClickListener {
            findNavController().navigate(R.id.manageRemindersBottomSheet)
        }
        binding.activeReminderLayout.toggleReminderButton.setOnClickListener {
            onToggleAlarmClicked()
        }
        binding.activeReminderLayout.editReminderButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToNewReminder(
                viewModel.activeReminder.value
            )
            findNavController().navigate(action)
        }
        binding.manageRemindersButton.setOnClickListener {
            findNavController().navigate(R.id.manageRemindersBottomSheet)
        }
    }

    private fun onActiveReminderUpdated(reminder: Reminder?) {
        binding.activeReminderLayout.root.isVisible = reminder != null
        binding.reminderNotFoundGroup.isVisible = reminder == null
        if (reminder == null) {
            return
        }
        with(binding.activeReminderLayout) {
            reminderTitle.text = reminder.name
            reminderInterval.text = reminder.timeIntervalSummary()
            reminderStart.setMinutesToHourlyTime(reminder.startTime)
            reminderEnd.setMinutesToHourlyTime(reminder.endTime)
            reminderDays.setDaysSummary(reminder)
            reminderColor.setBackgroundColorTintView(reminder.color)
        }
    }

    private fun setupPagerAndTabLayout() {
        pagerAdapter = DayLogHomeAdapter()
        binding.usagePager.pager.apply {
            adapter = pagerAdapter
            setPageFadeTransformer()
        }
        mediator = TabLayoutMediator(
            binding.usagePager.tabLayout,
            binding.usagePager.pager
        ) { _, _ -> }
        mediator?.attach()
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
                    findNavController().navigate(R.id.manageRemindersBottomSheet)
                }
            }
        }
    }

    private fun registerAlarmPermissionLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    viewModel.toggleAlarm()
                } else {
                    UiInterface.showSnackBar(getString(R.string.snack_permission_required))
                }
            }
        }
    }

    private fun onToggleAlarmClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !viewModel.canScheduleExactAlarms()) {
            showSnackWithPermissionAction()
        } else {
            viewModel.toggleAlarm()
        }
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
        requestPermissionLauncher.launch(
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse(PACKAGE_URI_NAME)
            }
        )
    }

    override fun onDestroyView() {
        mediator?.detach()
        mediator = null
        binding.usagePager.pager.adapter = null
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showItem(R.id.settings)
        menu.showItem(R.id.newReminder)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
