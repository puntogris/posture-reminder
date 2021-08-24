package com.puntogris.posture.ui.reminders.manage

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetManageRemindersBinding
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.ui.base.BaseBottomSheetFragment
import com.puntogris.posture.utils.UiInterface
import com.puntogris.posture.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageRemindersBottomSheet: BaseBottomSheetFragment<BottomSheetManageRemindersBinding>(R.layout.bottom_sheet_manage_reminders, false) {

    private val viewModel: ManageRemindersViewModel by viewModels()

    override fun initializeViews() {
        binding.bottomSheet = this
        setupRecyclerViewAdapter()
    }

    private fun setupRecyclerViewAdapter(){
        ManageReminderAdapter(
            requireContext(),
            { onSelectReminder(it) },
            { onEditReminder(it) },
            { onDeleteReminder(it) }
        ).let {
            binding.recyclerView.adapter = it
            subscribeUi(it)
        }
    }

    private fun subscribeUi(adapter: ManageReminderAdapter){
        viewModel.getAllReminders().observe(viewLifecycleOwner){
            adapter.updateList(it)
        }
    }

    private fun onSelectReminder(reminder: Reminder){
        lifecycleScope.launch {
            viewModel.updateCurrentReminder(reminder.reminderId)
            findNavController().navigateUp()
            requireParentFragment().UiInterface.showSnackBar(getString(R.string.snack_configuration_updated))
        }
    }

    private fun onEditReminder(reminder: Reminder){
        val action = ManageRemindersBottomSheetDirections
            .actionManageRemindersBottomSheetToNewReminderBottomSheet(reminder)
        findNavController().navigate(action)
    }

    private fun onDeleteReminder(reminder: Reminder){
        lifecycleScope.launch {
            viewModel.deleteReminder(reminder)
            showSnackBar(
                message = R.string.snack_delete_reminder_success,
                anchorView = binding.floatingActionButton){
                lifecycleScope.launch {
                    viewModel.insertReminder(reminder)
                }
            }
        }
    }

    fun onNewReminder(){
        findNavController().navigate(R.id.action_manageRemindersBottomSheet_to_newReminderBottomSheet)
    }
}