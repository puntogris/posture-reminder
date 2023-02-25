package com.puntogris.posture.ui.reminders.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentManageRemindersBinding
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.showSnackBar
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageRemindersFragment : Fragment(R.layout.fragment_manage_reminders) {

    private val viewModel: ManageRemindersViewModel by viewModels()
    private val binding by viewBinding(FragmentManageRemindersBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        setupObserver()
    }

    private fun setupListeners() {
        binding.buttonAddReminder.setOnClickListener {
            findNavController().navigate(R.id.action_manageReminders_to_newReminder)
        }
    }

    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.showTutorial.collect { show ->
                if (show) {
                    findNavController().navigate(R.id.manageReminderTutorialDialog)
                }
            }
        }
    }

    private fun subscribeUi(adapter: ManageReminderAdapter) {
        launchAndRepeatWithViewLifecycle {
            viewModel.reminders.collectLatest {
                adapter.submitList(it)
                binding.recyclerViewReminders.run {
                    post {
                        smoothScrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun setupViews() {
        val adapter = ManageReminderAdapter(
            context = requireContext(),
            selectListener = ::onSelectReminder,
            editListener = ::onEditReminder,
            deleteListener = ::onDeleteReminder
        )
        binding.recyclerViewReminders.adapter = adapter
        binding.recyclerViewReminders.setHasFixedSize(true)
        subscribeUi(adapter)
    }

    private fun onSelectReminder(reminder: Reminder) {
        lifecycleScope.launch {
            viewModel.updateReminder(reminder)
            findNavController().navigate(R.id.homeFragment)
            UiInterface.showSnackBar(getString(R.string.snack_configuration_updated))
        }
    }

    private fun onEditReminder(reminder: Reminder) {
        val action = ManageRemindersFragmentDirections.actionManageRemindersToNewReminder(reminder)
        findNavController().navigate(action)
    }

    private fun onDeleteReminder(reminder: Reminder) {
        lifecycleScope.launch {
            viewModel.deleteReminder(reminder)
            showSnackBar(
                message = R.string.snack_delete_reminder_success,
                anchor = binding.buttonAddReminder,
            ) {
                viewModel.insertReminder(reminder)
            }
        }
    }
}
