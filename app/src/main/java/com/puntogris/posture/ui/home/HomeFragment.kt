package com.puntogris.posture.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentHomeBinding
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.framework.alarm.AlarmStatus
import com.puntogris.posture.utils.PermissionsManager
import com.puntogris.posture.utils.Utils
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
class HomeFragment : Fragment(R.layout.fragment_home), MenuProvider {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private var mediator: TabLayoutMediator? = null
    private lateinit var pagerAdapter: DayLogHomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.animationPanda.setPadding(0, 0, -100, -110)
        setupListeners()
        setupObservers()
        setupPagerAndTabLayout()
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupObservers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.isAlarmActive.collectLatest {
               setToggleButton( binding.layoutActiveReminder.buttonToggleReminderState, it)
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.isPandaAnimationEnabled.collectLatest {
                binding.animationPanda.isVisible = it
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
        with(binding.layoutActiveReminder) {
            buttonSelectReminder.setOnClickListener {
                findNavController().navigate(R.id.manageRemindersFragment)
            }
            buttonToggleReminderState.setOnClickListener {
                onToggleAlarmClicked()
            }
            imageViewEditReminder.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeToNewReminder(
                    viewModel.activeReminder.value
                )
                findNavController().navigate(action)
            }
        }
        binding.buttonManageReminders.setOnClickListener {
            findNavController().navigate(R.id.manageRemindersFragment)
        }
    }

    private fun onActiveReminderUpdated(reminder: Reminder?) {
        binding.layoutActiveReminder.root.isVisible = reminder != null
        binding.groupReminderNotFound.isVisible = reminder == null
        if (reminder == null) {
            return
        }
        with(binding.layoutActiveReminder) {
            textViewReminderTitleValue.text = reminder.name
            textViewReminderIntervalValue.text = reminder.timeIntervalSummary()
            setMinutesToHourlyTime(textViewReminderStartValue, reminder.startTime)
            setMinutesToHourlyTime(textViewReminderEndValue, reminder.endTime)
            setDaysSummary(textViewReminderDaysValue, reminder)
            viewReminderColor.setBackgroundColorTintView(reminder.color)
        }
    }

    private fun setupPagerAndTabLayout() {
        pagerAdapter = DayLogHomeAdapter()
        binding.layoutLogsSummary.viewPager.apply {
            adapter = pagerAdapter
            setPageFadeTransformer()
        }
        mediator = TabLayoutMediator(
            binding.layoutLogsSummary.tabLayout,
            binding.layoutLogsSummary.viewPager
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
                    findNavController().navigate(R.id.manageRemindersFragment)
                }
            }
        }
    }

    private fun onToggleAlarmClicked() {
        val needsAppsPermissions = PermissionsManager.needsPermissionForApp(requireContext())
        if (!viewModel.isAlarmActive.value && needsAppsPermissions) {
            findNavController().navigate(R.id.permissionsFragment)
        } else {
            viewModel.toggleAlarm()
        }
    }

    override fun onDestroyView() {
        mediator?.detach()
        mediator = null
        binding.layoutLogsSummary.viewPager.adapter = null
        super.onDestroyView()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.showItem(R.id.settings)
        menu.showItem(R.id.newReminder)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
}
