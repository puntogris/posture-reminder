package com.puntogris.posture.ui.reminders.new_edit

import android.text.InputType
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.maxkeppeler.sheets.color.ColorSheet
import com.maxkeppeler.sheets.core.IconButton
import com.maxkeppeler.sheets.core.SheetStyle
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.type.InputEditText
import com.maxkeppeler.sheets.input.type.InputRadioButtons
import com.maxkeppeler.sheets.options.OptionsSheet
import com.maxkeppeler.sheets.time_clock.ClockTimeSheet
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetNewReminderBinding
import com.puntogris.posture.ui.base.BaseBindingBottomSheetFragment
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.utils.constants.Constants.INTERVAL_KEY
import com.puntogris.posture.utils.constants.Constants.SOUND_PICKER_KEY
import com.puntogris.posture.utils.constants.Constants.TIME_UNIT_KEY
import com.puntogris.posture.utils.constants.Constants.VIBRATION_PICKER_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class NewReminderBottomSheet : BaseBindingBottomSheetFragment<BottomSheetNewReminderBinding>(
    R.layout.bottom_sheet_new_reminder,
    true
) {
    private val viewModel: NewReminderViewModel by viewModels()

    override fun initializeViews() {
        binding.bottomSheet = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setupReminderRvAdapter()
        setFragmentResultListeners()
    }

    private fun setupReminderRvAdapter() {
        ReminderItemAdapter(requireContext()) { onReminderItemClicked(it) }.let {
            binding.recyclerView.adapter = it
            subscribeUi(it)
        }
    }

    private fun subscribeUi(adapter: ReminderItemAdapter) {
        launchAndRepeatWithViewLifecycle {
            viewModel.reminder.collect {
                adapter.updateConfigData(it)
            }
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

    fun onSaveReminder() {
        lifecycleScope.launch {
            when (val result = viewModel.saveReminder()) {
                is Result.Error -> {
                    showSnackBar(result.error)
                }
                is Result.Success -> {
                    dismiss()
                    requireParentFragment().UiInterface.showSnackBar(getString(R.string.snack_create_reminder_success))
                }
            }
        }
    }

    private fun onReminderItemClicked(reminderUi: ReminderUi) {
        when (reminderUi) {
            is ReminderUi.Color -> openColorPicker()
            is ReminderUi.Item.Interval -> openIntervalPicker()
            is ReminderUi.Name -> viewModel.saveReminderName(reminderUi.value)
            is ReminderUi.Item.Days -> openDaysPicker()
            is ReminderUi.Item.End -> openTimePicker(reminderUi)
            is ReminderUi.Item.Start -> openTimePicker(reminderUi)
            is ReminderUi.Item.Sound -> onSoundPicker()
            is ReminderUi.Item.Vibration -> onVibrationPicker()
        }
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
        InputSheet().show(requireParentFragment().requireContext()) {
            style(SheetStyle.DIALOG)
            title(R.string.time_interval_title)
            with(InputRadioButtons(TIME_UNIT_KEY) {
                label(R.string.time_unit_title)
                options(
                    mutableListOf(
                        this@NewReminderBottomSheet.getString(R.string.minutes),
                        this@NewReminderBottomSheet.getString(R.string.hours)
                    )
                )
                selected(0)
            })
            with(InputEditText(INTERVAL_KEY) {
                required()
                viewModel.reminder.value.timeInterval.let {
                    if (it != 0) defaultValue(it.toString())
                }
                closeIconButton(IconButton(R.drawable.ic_baseline_close_24))
                endIconMode(TextInputLayout.END_ICON_CLEAR_TEXT)
                label(R.string.time_interval_subtitle)
                hint(R.string.time_hint)
                inputType(InputType.TYPE_CLASS_NUMBER)
            })
            onNegative(R.string.action_cancel)
            onPositive(R.string.action_save) {
                val timeUnit = it.getInt(TIME_UNIT_KEY)
                val interval = it.getString(INTERVAL_KEY, "0").toInt()
                if (interval != 0) {
                    viewModel.saveTimeInterval(if (timeUnit == 0) interval else interval * 60)
                } else {
                    showSnackBar(R.string.snack_time_interval_cant_be_zero)
                }
            }
        }
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
        ClockTimeSheet().show(requireParentFragment().requireContext()) {
            style(SheetStyle.DIALOG)
            currentTime(viewModel.getDefaultClockTimeInMillis(code))
            title(
                if (code is ReminderUi.Item.Start) R.string.start_time_title
                else R.string.end_time_title
            )
            onNegative(R.string.action_cancel)
            onPositive { milliseconds: Long, _, _ ->
                if (code is ReminderUi.Item.Start) viewModel.saveStartTime(milliseconds)
                else viewModel.saveEndTime(milliseconds)
            }
        }
    }
}