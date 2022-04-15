package com.puntogris.posture.ui.reminders.manage

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetManageRemindersBinding
import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.navigateTo
import com.puntogris.posture.utils.extensions.setupAsFullScreen
import com.puntogris.posture.utils.extensions.showSnackBar
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageRemindersBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: ManageRemindersViewModel by viewModels()
    private val binding by viewBinding(BottomSheetManageRemindersBinding::bind)

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_manage_reminders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewAdapter()
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.addReminderButton.setOnClickListener {
            navigateTo(R.id.action_manageReminders_to_newReminder)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setupAsFullScreen(isDraggable = false)
        }
    }

    private fun setupRecyclerViewAdapter() {
        ManageReminderAdapter(
            requireContext(),
            { onSelectReminder(it) },
            { onEditReminder(it) },
            { onDeleteReminder(it) }
        ).also {
            binding.recyclerView.adapter = it
            subscribeUi(it)
        }
    }

    private fun subscribeUi(adapter: ManageReminderAdapter) {
        viewModel.savedReminders.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }

    private fun onSelectReminder(reminder: Reminder) {
        lifecycleScope.launch {
            viewModel.updateCurrentReminder(reminder)
            navigateTo(R.id.homeFragment)
            requireParentFragment().UiInterface.showSnackBar(getString(R.string.snack_configuration_updated))
        }
    }

    private fun onEditReminder(reminder: Reminder) {
        val action =
            ManageRemindersBottomSheetDirections.actionManageRemindersToNewReminder(reminder)
        findNavController().navigate(action)
    }

    private fun onDeleteReminder(reminder: Reminder) {
        lifecycleScope.launch {
            viewModel.deleteReminder(reminder)
            showSnackBar(
                message = R.string.snack_delete_reminder_success,
                anchorView = binding.addReminderButton
            ) {
                lifecycleScope.launch {
                    viewModel.insertReminder(reminder)
                }
            }
        }
    }
}
