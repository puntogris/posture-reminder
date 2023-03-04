package com.puntogris.posture.ui.reminders.configuration

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetNewReminderBinding
import com.puntogris.posture.ui.reminders.configuration.pickers.ColorPickerDialog
import com.puntogris.posture.ui.reminders.configuration.pickers.DaysPickerDialog
import com.puntogris.posture.ui.reminders.configuration.pickers.IntervalPickerDialog
import com.puntogris.posture.ui.reminders.configuration.pickers.ReminderNamePickerDialog
import com.puntogris.posture.ui.reminders.configuration.pickers.SoundPickerDialog
import com.puntogris.posture.ui.reminders.configuration.pickers.VibrationPickerDialog
import com.puntogris.posture.utils.ReminderUi
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.constants.Constants.EDIT_REMINDER_FLOW
import com.puntogris.posture.utils.constants.Constants.MINUTES_IN_AN_HOUR
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.extensions.getHours
import com.puntogris.posture.utils.extensions.getMinutes
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.setupAsFullScreen
import com.puntogris.posture.utils.extensions.showSnackBar
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewReminderBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: NewReminderViewModel by viewModels()
    private val binding by viewBinding(BottomSheetNewReminderBinding::bind)
    private val args: NewReminderBottomSheetArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_new_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupReminderRvAdapter()
    }

    private fun setupListeners() {
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.buttonSaveReminder.setOnClickListener {
            onSaveReminder()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setupAsFullScreen(isDraggable = true)
        }
    }

    private fun setupReminderRvAdapter() {
        ReminderItemAdapter(requireContext(), ::onReminderItemClicked).also {
            binding.recyclerViewReminderItems.adapter = it
            subscribeUi(it)
        }
    }

    private fun subscribeUi(adapter: ReminderItemAdapter) {
        launchAndRepeatWithViewLifecycle {
            viewModel.reminder.collect(adapter::updateConfigData)
        }
    }

    private fun onSaveReminder() {
        lifecycleScope.launch {
            when (val result = viewModel.saveReminder()) {
                is Result.Error -> {
                    showSnackBar(result.error, anchorView = binding.buttonSaveReminder)
                }
                is Result.Success -> {
                    UiInterface.showSnackBar(getString(R.string.snack_create_reminder_success))
                    if (args.flow == EDIT_REMINDER_FLOW) {
                        dismiss()
                    } else {
                        findNavController().navigate(R.id.manageRemindersFragment)
                    }
                }
                else -> Unit
            }
        }
    }

    private fun onReminderItemClicked(reminderUi: ReminderUi) {
        when (reminderUi) {
            is ReminderUi.Color -> openColorPicker()
            is ReminderUi.Item.Interval -> openIntervalPicker()
            is ReminderUi.Item.Name -> openNamePicker()
            is ReminderUi.Item.Days -> openDaysPicker()
            is ReminderUi.Item.End -> openEndTimePicker()
            is ReminderUi.Item.Start -> openStartTimePicker()
            is ReminderUi.Item.Sound -> onSoundPicker()
            is ReminderUi.Item.Vibration -> onVibrationPicker()
        }
    }

    private fun openNamePicker() {
        ReminderNamePickerDialog(
            currentName = viewModel.reminder.value.name,
            onPickedAction = viewModel::saveReminderName
        ).show(parentFragmentManager, "NAME_PICKER_DIALOG")
    }

    private fun onSoundPicker() {
        SoundPickerDialog(
            currentSound = viewModel.reminder.value.soundUri,
            onPickedAction = viewModel::saveReminderSoundPattern
        ).show(parentFragmentManager, "SOUND_PICKER_DIALOG")
    }

    private fun onVibrationPicker() {
        VibrationPickerDialog(
            currentPosition = viewModel.reminder.value.vibrationPattern,
            onPickedAction = viewModel::saveReminderVibrationPattern
        ).show(parentFragmentManager, "VIBRATION_PICKER_DIALOG")
    }

    private fun openColorPicker() {
        ColorPickerDialog(
            viewModel::saveReminderColor
        ).show(parentFragmentManager, "COLOR_PICKER_DIALOG")
    }

    private fun openIntervalPicker() {
        IntervalPickerDialog(
            currentInterval = viewModel.reminder.value.timeInterval,
            onPickedAction = viewModel::saveTimeInterval
        ).show(parentFragmentManager, "TIME_INTERVAL_DIALOG")
    }

    private fun openDaysPicker() {
        DaysPickerDialog(
            currentDays = viewModel.reminder.value.alarmDays,
            onPickedAction = viewModel::saveReminderDays
        ).show(parentFragmentManager, "DAYS_PICKER_DIALOG")
    }

    private fun openStartTimePicker() {
        openTimePicker(
            title = R.string.start_time_title,
            currentTime = viewModel.reminder.value.startTime,
            onSaveAction = viewModel::saveStartTime
        )
    }

    private fun openEndTimePicker() {
        openTimePicker(
            title = R.string.end_time_title,
            currentTime = viewModel.reminder.value.endTime,
            onSaveAction = viewModel::saveEndTime
        )
    }

    private fun openTimePicker(title: Int, currentTime: Int, onSaveAction: (Int) -> Unit) {
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setInputMode(INPUT_MODE_CLOCK)
            .setTitleText(title)
            .setPositiveButtonText(R.string.action_done)
            .setNegativeButtonText(R.string.action_cancel)
            .apply {
                if (currentTime != -1) {
                    setHour(currentTime.getHours())
                    setMinute(currentTime.getMinutes())
                }
            }.build()
            .apply {
                addOnPositiveButtonClickListener {
                    onSaveAction((hour * MINUTES_IN_AN_HOUR) + minute)
                }
            }.show(parentFragmentManager, "TIME_PICKER_DIALOG")
    }
}
