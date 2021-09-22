package com.puntogris.posture.ui.reminders.new_edit

import android.text.InputType
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.maxkeppeler.sheets.color.ColorSheet
import com.maxkeppeler.sheets.core.IconButton
import com.maxkeppeler.sheets.core.SheetStyle
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.type.InputEditText
import com.maxkeppeler.sheets.input.type.InputRadioButtons
import com.maxkeppeler.sheets.options.OptionsSheet
import com.maxkeppeler.sheets.time_clock.ClockTimeSheet
import com.puntogris.posture.NavigationDirections
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetNewReminderBinding
import com.puntogris.posture.model.ReminderId
import com.puntogris.posture.model.ReminderUi
import com.puntogris.posture.model.Result
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.ui.base.BaseBottomSheetFragment
import com.puntogris.posture.ui.reminders.manage.ManageRemindersBottomSheet
import com.puntogris.posture.utils.Constants.DATA_KEY
import com.puntogris.posture.utils.Constants.INTERVAL_KEY
import com.puntogris.posture.utils.Constants.SOUND_PICKER_KEY
import com.puntogris.posture.utils.Constants.TIME_UNIT_KEY
import com.puntogris.posture.utils.Constants.VIBRATION_PICKER_KEY
import com.puntogris.posture.utils.UiInterface
import com.puntogris.posture.utils.Utils
import com.puntogris.posture.utils.Utils.getDateFromMinutesSinceMidnight
import com.puntogris.posture.utils.showSnackBar
import com.puntogris.posture.utils.timeWithZoneOffset
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class NewReminderBottomSheet : BaseBottomSheetFragment<BottomSheetNewReminderBinding>(
    R.layout.bottom_sheet_new_reminder,
    true
) {
    private val viewModel: NewReminderViewModel by viewModels()
    private val args: NewReminderBottomSheetArgs by navArgs()

    override fun initializeViews() {
        binding.bottomSheet = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        checkIfIsNotNewReminder()
        setupReminderRvAdapter()
        setFragmentResultListeners()

    }

    private fun checkIfIsNotNewReminder(){
        args.reminder?.let {
            viewModel.updateReminder(it.copy())
        }
    }

    private fun setupReminderRvAdapter(){
        ReminderItemAdapter(requireContext()) { onReminderItemClicked(it) }.let {
            binding.recyclerView.adapter = it
            subscribeUi(it)
        }
    }

    private fun subscribeUi(adapter: ReminderItemAdapter) {
        viewModel.reminder.observe(viewLifecycleOwner) {
            adapter.updateConfigData(it)
        }
    }

    private fun setFragmentResultListeners(){
        setFragmentResultListener(VIBRATION_PICKER_KEY){ _, bundle ->
            viewModel.saveReminderVibrationPattern(bundle.getInt(DATA_KEY))
        }
        setFragmentResultListener(SOUND_PICKER_KEY){ _, bundle ->
            viewModel.saveReminderSoundPattern(bundle.getParcelable(DATA_KEY))
        }
    }

    fun onSaveReminder() {
        if (!viewModel.isReminderValid()){
            lifecycleScope.launch {
                handleResultOfSavingReminder(viewModel.saveReminder())
            }
        }else{
            showSnackBar(R.string.snack_reminder_not_valid)
        }
    }

    private fun handleResultOfSavingReminder(result: Result<ReminderId>){
        when(result){
            is Result.Error -> {
                showSnackBar(R.string.snack_create_reminder_error)
            }
            is Result.Success -> {
                dismiss()
                requireParentFragment().UiInterface.showSnackBar(getString(R.string.snack_create_reminder_success))
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

    private fun onSoundPicker(){
        val action = NewReminderBottomSheetDirections
            .actionNewReminderBottomSheetToSoundSelectorDialog(viewModel.reminder.value!!.soundUri)
        findNavController().navigate(action)
    }

    private fun onVibrationPicker(){
        val action = NewReminderBottomSheetDirections
            .actionNewReminderBottomSheetToVibrationSelectorDialog(viewModel.reminder.value!!.vibrationPattern)
        findNavController().navigate(action)
    }

    private fun openColorPicker() {
        ColorSheet().show(requireParentFragment().requireContext()) {
            title(R.string.color_picker_title)
            disableSwitchColorView()
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
                this@NewReminderBottomSheet.viewModel.reminder.value?.timeInterval?.let {
                    if (it != 0) defaultValue(it.toString())
                }
                closeIconButton(IconButton(R.drawable.ic_baseline_close_24))
                endIconMode(TextInputLayout.END_ICON_CLEAR_TEXT)
                label(R.string.time_interval_subtitle)
                hint(R.string.time_hint)
                inputType(InputType.TYPE_CLASS_NUMBER)
            })
            onPositive(R.string.action_save) {
                val timeUnit = it.getInt(TIME_UNIT_KEY)
                val interval = it.getString(INTERVAL_KEY, "0").toInt()
                if (interval != 0) {
                    viewModel.saveTimeInterval(if (timeUnit == 0) interval else interval * 60)
                } else {
                    this@NewReminderBottomSheet.showSnackBar(R.string.snack_time_interval_cant_be_zero)
                }
            }
        }
    }

    private fun openDaysPicker() {
        val alarmDaysString = resources.getStringArray(R.array.alarmDays)
        val savedList = viewModel.reminder.value?.alarmDays
        val options = Utils.getSavedOptions(savedList, alarmDaysString)

        OptionsSheet().show(requireParentFragment().requireContext()) {
            style(SheetStyle.DIALOG)
            title(R.string.alarm_days_title)
            multipleChoices(true)
            with(options)
            onPositiveMultiple(R.string.action_save) { indices, _ ->
                viewModel.saveReminderDays(indices.sorted())
            }
        }
    }

    private fun openTimePicker(code: ReminderUi.Item) {
        ClockTimeSheet().show(requireParentFragment().requireContext()) {
            style(SheetStyle.DIALOG)
            currentTime(getDefaultClockTimeInMillis(code))
            title(
                if (code is ReminderUi.Item.Start) R.string.start_time_title
                else R.string.end_time_title
            )
            onPositive { milliseconds: Long, _, _ ->
                if (code is ReminderUi.Item.Start) viewModel.saveStartTime(milliseconds)
                else viewModel.saveEndTime(milliseconds)
            }
        }
    }

    private fun getDefaultClockTimeInMillis(code: ReminderUi.Item): Long{
        val isNewReminder = viewModel.reminder.value?.reminderId.isNullOrBlank()

        val date = if (isNewReminder) Date()
        else getDateFromMinutesSinceMidnight(getReminderTime(code))

        return date.timeWithZoneOffset
    }

    private fun getReminderTime(code: ReminderUi.Item): Int{
        return if (code is ReminderUi.Item.Start) viewModel.reminder.value!!.startTime
        else viewModel.reminder.value!!.endTime
    }
}