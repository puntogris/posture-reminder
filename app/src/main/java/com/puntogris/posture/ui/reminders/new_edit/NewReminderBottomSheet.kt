package com.puntogris.posture.ui.reminders.new_edit

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.maxkeppeler.sheets.color.ColorSheet
import com.maxkeppeler.sheets.core.SheetStyle
import com.maxkeppeler.sheets.options.OptionsSheet
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetNewReminderBinding
import com.puntogris.posture.utils.ReminderUi
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.Utils
import com.puntogris.posture.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.utils.constants.Constants.EDIT_REMINDER_FLOW
import com.puntogris.posture.utils.constants.Constants.MINUTES_IN_AN_HOR
import com.puntogris.posture.utils.constants.Constants.SOUND_PICKER_KEY
import com.puntogris.posture.utils.constants.Constants.VIBRATION_PICKER_KEY
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
        setFragmentResultListeners()
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

    private fun setFragmentResultListeners() {
        setFragmentResultListener(VIBRATION_PICKER_KEY) { _, bundle ->
            viewModel.saveReminderVibrationPattern(bundle.getInt(DATA_KEY))
        }
        setFragmentResultListener(SOUND_PICKER_KEY) { _, bundle ->
            viewModel.saveReminderSoundPattern(bundle.getParcelable(DATA_KEY))
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
                        findNavController().navigate(R.id.manageRemindersFragment)
                    } else {
                        dismiss()
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
            is ReminderUi.Item.End -> openTimePicker(reminderUi)
            is ReminderUi.Item.Start -> openTimePicker(reminderUi)
            is ReminderUi.Item.Sound -> onSoundPicker()
            is ReminderUi.Item.Vibration -> onVibrationPicker()
        }
    }

    private fun openNamePicker() {
        ReminderNamePickerDialog(viewModel.reminder.value.name) {
            viewModel.saveReminderName(it)
        }.show(parentFragmentManager, "REMINDER_NAME_DIALOG")
    }

    private fun onSoundPicker() {
        val action = NewReminderBottomSheetDirections.actionNewReminderToSoundSelector(
            viewModel.reminder.value.soundUri
        )
        findNavController().navigate(action)
    }

    private fun onVibrationPicker() {
        val action = NewReminderBottomSheetDirections.actionNewReminderToVibrationSelector(
            viewModel.reminder.value.vibrationPattern
        )
        findNavController().navigate(action)
    }

    private fun openColorPicker() {
        ColorSheet().show(requireParentFragment().requireContext()) {
            title(R.string.color_picker_title)
            disableSwitchColorView()
            onNegative(R.string.action_cancel)
            onPositive { color -> viewModel.saveReminderColor(color) }
        }
    }

    private fun openIntervalPicker() {
        IntervalPickerDialog(viewModel.reminder.value.timeInterval) {
            if (it != null) viewModel.saveTimeInterval(it)
        }.show(parentFragmentManager, "TIME_INTERVAL_DIALOG")
    }

    private fun openDaysPicker() {
        val alarmDaysString = resources.getStringArray(R.array.alarmDays)
        val savedList = viewModel.reminder.value.alarmDays
        val options = Utils.getSavedOptions(savedList, alarmDaysString)

        OptionsSheet().show(requireParentFragment().requireContext()) {
            style(SheetStyle.DIALOG)
            title(R.string.alarm_days_title)
            multipleChoices(true)
            with(options)
            onNegative(R.string.action_cancel)
            onPositiveMultiple(R.string.action_save) { indices, _ ->
                viewModel.saveReminderDays(indices.sorted())
            }
        }
    }

    private fun openTimePicker(code: ReminderUi.Item) {
        val title = if (code is ReminderUi.Item.Start) {
            R.string.start_time_title
        } else {
            R.string.end_time_title
        }
        val defaultTime = viewModel.getDefaultClockTimeInMillis(code)
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setInputMode(INPUT_MODE_CLOCK)
            .setTitleText(title)
            .setPositiveButtonText(R.string.action_done)
            .setNegativeButtonText(R.string.action_cancel)
            .build()
            .apply {
                if (defaultTime != -1) {
                    hour = defaultTime.getHours()
                    minute = defaultTime.getMinutes()
                }
                addOnPositiveButtonClickListener {
                    val time = (hour * MINUTES_IN_AN_HOR) + minute
                    if (code is ReminderUi.Item.Start) {
                        viewModel.saveStartTime(time)
                    } else {
                        viewModel.saveEndTime(time)
                    }
                }
            }.show(parentFragmentManager, "TIMEPICKER_DIALOG")
    }
}
