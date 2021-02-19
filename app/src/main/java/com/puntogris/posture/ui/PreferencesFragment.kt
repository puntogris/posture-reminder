package com.puntogris.posture.ui

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.textfield.TextInputLayout
import com.maxkeppeler.sheets.core.IconButton
import com.maxkeppeler.sheets.core.SheetStyle
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.type.InputEditText
import com.maxkeppeler.sheets.input.type.InputRadioButtons
import com.maxkeppeler.sheets.options.OptionsSheet
import com.maxkeppeler.sheets.time_clock.ClockTimeSheet
import com.puntogris.posture.R
import com.puntogris.posture.model.Report
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.Constants.ALARM_DAYS_PREFERENCE
import com.puntogris.posture.utils.Constants.BUG_REPORT_PREFERENCE
import com.puntogris.posture.utils.Constants.END_TIME_PREFERENCE
import com.puntogris.posture.utils.Constants.PANDA_ANIMATION_PREFERENCE
import com.puntogris.posture.utils.Constants.START_TIME_PREFERENCE
import com.puntogris.posture.utils.Constants.TIME_INTERVAL_PREFERENCE
import com.puntogris.posture.utils.Utils.getSavedOptionsArray
import com.puntogris.posture.utils.Utils.minutesFromMidnightToHourlyTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class PreferencesFragment: PreferenceFragmentCompat() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val alarmDaysString = resources.getStringArray(R.array.alarmDays)

        viewModel.reminder.observe(viewLifecycleOwner, {
            preference(START_TIME_PREFERENCE)?.summary = minutesFromMidnightToHourlyTime(it.startTime)
            preference(END_TIME_PREFERENCE)?.summary = minutesFromMidnightToHourlyTime(it.endTime)
            preference(TIME_INTERVAL_PREFERENCE)?.summary = it.timeIntervalSummary()
            preference(ALARM_DAYS_PREFERENCE)?.summary = it.alarmDaysSummary(alarmDaysString)
        })

        preference(PANDA_ANIMATION_PREFERENCE)?.setOnPreferenceClickListener {
            viewModel.enablePandaAnimation()
            createSnackBar(getString(R.string.panda_unlocked_message))
            true
        }

        preference(BUG_REPORT_PREFERENCE)?.setOnPreferenceClickListener {
            InputSheet().show(requireParentFragment().requireContext()) {
                style(SheetStyle.DIALOG)
                title(this@PreferencesFragment.getString(R.string.bug_report_title_pref))
                content(this@PreferencesFragment.getString(R.string.bug_report_summary_pref))
                with(InputEditText {
                    label(this@PreferencesFragment.getString(R.string.bug_report_name_title))
                    hint(this@PreferencesFragment.getString(R.string.name_hint))
                })
                with(InputEditText {
                    label(this@PreferencesFragment.getString(R.string.bug_report_email_title))
                    hint(this@PreferencesFragment.getString(R.string.email))
                })
                with(InputEditText {
                    required()
                    label(this@PreferencesFragment.getString(R.string.report_message_tittle))
                    hint(this@PreferencesFragment.getString(R.string.bug_report_dialog_hint))
                })
                onPositive(this@PreferencesFragment.getString(R.string.send_button)) {
                    val report = Report(it.getString("0").toString(), it.getString("1").toString(), it.getString("2").toString())
                    onResultSendReport(report)
                }
            }
            true
        }

        preference(START_TIME_PREFERENCE)?.setOnPreferenceClickListener {
            ClockTimeSheet().show(requireParentFragment().requireContext()) {
                style(SheetStyle.DIALOG)
                title(this@PreferencesFragment.getString(R.string.start_time_title))
                onPositive { milliseconds: Long, _, _ -> onResultStartTime(milliseconds) }
            }
            true
        }

        preference(END_TIME_PREFERENCE)?.setOnPreferenceClickListener {
            ClockTimeSheet().show(requireParentFragment().requireContext()) {
                style(SheetStyle.DIALOG)
                title(this@PreferencesFragment.getString(R.string.end_time_title))
                onPositive(this@PreferencesFragment.getString(R.string.save_button)) { milliseconds: Long, _, _ ->  onResultEndTime(milliseconds) }
            }
            true
        }

        preference(TIME_INTERVAL_PREFERENCE)?.setOnPreferenceClickListener {
            InputSheet().show(requireParentFragment().requireContext()) {
                style(SheetStyle.DIALOG)
                title(this@PreferencesFragment.getString(R.string.time_interval_title))
                with(InputRadioButtons {
                    label(this@PreferencesFragment.getString(R.string.time_unit_title))
                    options(mutableListOf(this@PreferencesFragment.getString(R.string.minutes), this@PreferencesFragment.getString(R.string.hours)))
                    selected(0)
                })
                with(InputEditText {
                    required()
                    this@PreferencesFragment.viewModel.reminder.value?.timeInterval?.let {
                        defaultValue(it.toString())
                    }
                    closeIconButton(IconButton(R.drawable.ic_baseline_close_24))
                    endIconMode(TextInputLayout.END_ICON_CLEAR_TEXT)
                    label(this@PreferencesFragment.getString(R.string.time_interval_pref_title))
                    hint(this@PreferencesFragment.getString(R.string.time_hint))
                    inputType(InputType.TYPE_CLASS_NUMBER)
                })
                onPositive(this@PreferencesFragment.getString(R.string.save_button)) {
                    try {

                        val valueTime = it.getString("1")
                        if (valueTime.isNullOrEmpty() || valueTime == "0" || valueTime.toString().first() == '0'){
                            this@PreferencesFragment.createSnackBar(this@PreferencesFragment.getString(
                                    R.string.time_interval_cant_be_zero
                                ))
                        }else{
                            val timeUnit = it.getInt("0")
                            val minutes = if (timeUnit == 0) valueTime.toInt() else (valueTime.toInt() * 60)
                            onResultTimeInterval(minutes)
                        }
                    }catch (e: Exception){
                        this@PreferencesFragment.createSnackBar(this@PreferencesFragment.getString(R.string.error_occurred))
                    }
                }
            }
            true
        }

        preference(ALARM_DAYS_PREFERENCE)?.setOnPreferenceClickListener {
            val savedList = viewModel.reminder.value?.alarmDays
            val options = getSavedOptionsArray(savedList, alarmDaysString)
            OptionsSheet().show(requireParentFragment().requireContext()) {
                style(SheetStyle.DIALOG)
                title(this@PreferencesFragment.getString(R.string.alarm_days_title))
                multipleChoices(true)
                with(*options)
                onPositiveMultiple(this@PreferencesFragment.getString(R.string.save_button)) { indices, _ -> onResultAlarmDays(
                    indices
                ) }
            }
            true
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun onResultStartTime(milliseconds: Long){
        lifecycleScope.launch {
            viewModel.saveStartTime(milliseconds.millisToMinutes())
            refreshAlarmsAndShowSnackBar()
        }
    }
    private fun onResultEndTime(milliseconds: Long){
        lifecycleScope.launch {
            viewModel.saveEndTime(milliseconds.millisToMinutes())
            refreshAlarmsAndShowSnackBar()
        }
    }

    private fun onResultSendReport(report:Report){
        lifecycleScope.launch {
            when(viewModel.sendReport(report)){
                RepoResult.Success -> createSnackBar(getString(R.string.report_sended_toast))
                RepoResult.Failure -> createSnackBar(getString(R.string.report_error_toast))
            }
        }
    }

    private fun onResultAlarmDays(selectedIndices: List<Int>){
        lifecycleScope.launch {
            viewModel.saveAlarmDays(selectedIndices.sorted())
            refreshAlarmsAndShowSnackBar()
        }
    }

    private fun onResultTimeInterval(interval: Int){
        lifecycleScope.launch {
            viewModel.saveTimeInterval(interval)
            refreshAlarmsAndShowSnackBar()
        }
    }

    private fun refreshAlarmsAndShowSnackBar(){
        if(viewModel.isAppActive()) viewModel.refreshAlarms()
        createSnackBar(getString(R.string.new_config_saved_toast))
    }
}
