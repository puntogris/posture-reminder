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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentHomeBinding
import com.puntogris.posture.framework.alarm.AlarmStatus
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.constants.Constants.PACKAGE_URI_NAME
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.navigateTo
import com.puntogris.posture.utils.extensions.setPageFadeTransformer
import com.puntogris.posture.utils.extensions.showItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private var mediator: TabLayoutMediator? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pandaAnimation.setPadding(0, 0, -100, -110)

        viewModel.isPandaAnimationEnabled.observe(viewLifecycleOwner) {
            if (it != null) binding.pandaAnimation.isVisible = it
        }

        binding.activeReminderLayout.selectReminderButton.setOnClickListener {
            navigateTo(R.id.manageRemindersBottomSheet)
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
            navigateTo(R.id.manageRemindersBottomSheet)
        }
        viewModel.isAlarmActive.observe(viewLifecycleOwner) {
            binding.activeReminderLayout.toggleReminderButton.setToggleButton(it)
        }
        viewModel.activeReminder.observe(viewLifecycleOwner) { reminder ->
            reminder?.let {
                binding.activeReminderLayout.reminderTitle.text = it.name
                binding.activeReminderLayout.reminderInterval.text = it.timeIntervalSummary()
                binding.activeReminderLayout.reminderStart.setMinutesToHourlyTime(it.startTime)
                binding.activeReminderLayout.reminderEnd.setMinutesToHourlyTime(it.endTime)
                binding.activeReminderLayout.reminderDays.setDaysSummary(it)
                binding.activeReminderLayout.reminderColor.setBackgroundColorTintView(reminder.color)
            }
            binding.activeReminderLayout.root.isVisible = reminder != null
            binding.reminderNotFoundGroup.isVisible = reminder == null
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
        mediator = TabLayoutMediator(
            binding.usagePager.tabLayout,
            binding.usagePager.pager
        ) { _, _ -> }
        mediator?.attach()
    }

    private fun subscribePager(adapter: DayLogHomeAdapter) {
        viewModel.getLastTwoDaysHistory.observe(viewLifecycleOwner, adapter::updateList)
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
