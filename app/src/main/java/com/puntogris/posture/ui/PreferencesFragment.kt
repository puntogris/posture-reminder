package com.puntogris.posture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceFragmentCompat
import com.maxkeppeler.bottomsheets.input.InputSheet
import com.maxkeppeler.bottomsheets.input.type.InputEditText
import com.maxkeppeler.bottomsheets.input.type.InputRadioButtons
import com.maxkeppeler.bottomsheets.options.OptionsSheet
import com.maxkeppeler.bottomsheets.time_clock.ClockTimeSheet
import com.puntogris.posture.R
import com.puntogris.posture.model.RepoResult
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

@AndroidEntryPoint
class PreferencesFragment: PreferenceFragmentCompat() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
                title(this@PreferencesFragment.getString(R.string.bug_report_title_pref))
                content(this@PreferencesFragment.getString(R.string.bug_report_summary_pref))
                with(InputEditText {
                    required()
                    label(this@PreferencesFragment.getString(R.string.report_message_tittle))
                    hint(this@PreferencesFragment.getString(R.string.bug_report_dialog_hint))
                    onPositive(this@PreferencesFragment.getString(R.string.send_button)) {
                        onResultSendReport(value.toString())
                    }
                })
            }
            true
        }

        preference(START_TIME_PREFERENCE)?.setOnPreferenceClickListener {
            ClockTimeSheet().show(requireParentFragment().requireContext()) {
                title(this@PreferencesFragment.getString(R.string.start_time_title))
                onPositive { milliseconds: Long, _, _ -> onResultStartTime(milliseconds) }
            }
            true
        }

        preference(END_TIME_PREFERENCE)?.setOnPreferenceClickListener {
            ClockTimeSheet().show(requireParentFragment().requireContext()) {
                title(this@PreferencesFragment.getString(R.string.end_time_title))
                onPositive(this@PreferencesFragment.getString(R.string.save_button)) { milliseconds: Long, _, _ ->  onResultEndTime(milliseconds) }
            }
            true
        }

        preference(TIME_INTERVAL_PREFERENCE)?.setOnPreferenceClickListener {
            val list = resources.getStringArray(R.array.timeInterval).toMutableList()
            val valueList = resources.getIntArray(R.array.timeIntervalValues).toMutableList()
            val optionSelected = valueList.indexOf(viewModel.reminder.value?.timeInterval)

            InputSheet().show(requireParentFragment().requireContext()){
                title(this@PreferencesFragment.getString(R.string.time_interval_title))
                with(InputRadioButtons{
                    required()
                    options(list)
                    selected(optionSelected)
                })
                onPositive(this@PreferencesFragment.getString(R.string.save_button)){ onResultTimeInterval(valueList[it.getInt("0")]) }
            }
            true
        }

        preference(ALARM_DAYS_PREFERENCE)?.setOnPreferenceClickListener {
            val savedList = viewModel.reminder.value?.alarmDays
            val options = getSavedOptionsArray(savedList, alarmDaysString)
            OptionsSheet().show(requireParentFragment().requireContext()) {
                title(this@PreferencesFragment.getString(R.string.alarm_days_title))
                multipleChoices(true)
                with(*options)
                onPositiveMultiple(this@PreferencesFragment.getString(R.string.save_button)) { indices , _ -> onResultAlarmDays(indices) }
            }
            true
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun onResultStartTime(milliseconds :Long){
        lifecycleScope.launch {
            viewModel.saveStartTime(milliseconds.millisToMinutes())
            refreshAlarmsAndShowSnackBar()
        }
    }
    private fun onResultEndTime(milliseconds :Long){
        lifecycleScope.launch {
            viewModel.saveEndTime(milliseconds.millisToMinutes())
            refreshAlarmsAndShowSnackBar()
        }
    }

    private fun onResultSendReport(message: String){
        lifecycleScope.launch {
            when(viewModel.sendReport(message)){
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
