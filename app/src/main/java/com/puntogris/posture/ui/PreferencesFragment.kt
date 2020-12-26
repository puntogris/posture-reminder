package com.puntogris.posture.ui

import android.os.Bundle
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
import com.puntogris.posture.utils.Constants.ALARM_DAYS_PREFERENCE
import com.puntogris.posture.utils.Constants.BUG_REPORT_PREFERENCE
import com.puntogris.posture.utils.Constants.END_TIME_PREFERENCE
import com.puntogris.posture.utils.Constants.PANDA_ANIMATION_PREFERENCE
import com.puntogris.posture.utils.Constants.START_TIME_PREFERENCE
import com.puntogris.posture.utils.Constants.TIME_INTERVAL_PREFERENCE
import com.puntogris.posture.utils.Utils.getSavedOptionsArray
import com.puntogris.posture.utils.Utils.minutesFromMidnightToHourlyTime
import com.puntogris.posture.utils.createSnackBar
import com.puntogris.posture.utils.millisToMinutes

import com.puntogris.posture.utils.preference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreferencesFragment: PreferenceFragmentCompat() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        val alarmDaysString = resources.getStringArray(R.array.alarmDays)

        lifecycleScope.launchWhenStarted {
            viewModel.reminder.observe(viewLifecycleOwner, {
                preference(START_TIME_PREFERENCE)?.summary = minutesFromMidnightToHourlyTime(it.startTime)
                preference(END_TIME_PREFERENCE)?.summary = minutesFromMidnightToHourlyTime(it.endTime)
                preference(TIME_INTERVAL_PREFERENCE)?.summary = it.timeIntervalSummary()
                preference(ALARM_DAYS_PREFERENCE)?.summary = it.alarmDaysSummary(alarmDaysString)
            })
        }

        preference(PANDA_ANIMATION_PREFERENCE)?.setOnPreferenceClickListener {
            viewModel.enablePandaAnimation()
            createSnackBar(getString(R.string.panda_unlocked_message))
            true
        }

        preference(BUG_REPORT_PREFERENCE)?.setOnPreferenceClickListener {
            InputSheet().build(requireContext()) {
                title(this@PreferencesFragment.getString(R.string.bug_report_title_pref))
                content(this@PreferencesFragment.getString(R.string.bug_report_summary_pref))
                with(InputEditText {
                    required()
                    label(this@PreferencesFragment.getString(R.string.report_message_tittle))
                    hint(this@PreferencesFragment.getString(R.string.bug_report_dialog_hint))
                    resultListener { value ->
                        viewModel.sendReport(value.toString())
                    }
                })
            }.show(parentFragmentManager, "")
            true
        }

        preference(START_TIME_PREFERENCE)?.setOnPreferenceClickListener {
            ClockTimeSheet().build(requireContext()) {
                title(this@PreferencesFragment.getString(R.string.start_time_title))
                onPositive(this@PreferencesFragment.getString(R.string.save_button)) { clockTimeInMillis ->
                    viewModel.viewModelScope.launch {
                        viewModel.saveStartTime(clockTimeInMillis.millisToMinutes() + 60)
                        refreshAlarmsAndShowSnackBar()
                    }
                }
            }.show(parentFragmentManager, "")
            true
        }

        preference(END_TIME_PREFERENCE)?.setOnPreferenceClickListener {
            ClockTimeSheet().build(requireContext()) {
                title(this@PreferencesFragment.getString(R.string.end_time_title))
                onPositive(this@PreferencesFragment.getString(R.string.save_button)) { clockTimeInMillis ->
                    lifecycleScope.launch {
                        viewModel.saveEndTime(clockTimeInMillis.millisToMinutes() +60)
                        refreshAlarmsAndShowSnackBar()
                    }
                }
            }.show(parentFragmentManager, "")
            true
        }

        preference(TIME_INTERVAL_PREFERENCE)?.setOnPreferenceClickListener {
            val list = resources.getStringArray(R.array.timeInterval).toMutableList()
            val valueList = resources.getIntArray(R.array.timeIntervalValues).toMutableList()
            InputSheet().build(requireContext()) {
                title(this@PreferencesFragment.getString(R.string.time_interval_title))
                with(InputRadioButtons{
                    required()
                    options(list)
                    selected(0)
                })
                onPositive(this@PreferencesFragment.getString(R.string.save_button)){
                    lifecycleScope.launch {
                        viewModel.saveTimeInterval(valueList[it["0"] as Int])
                        refreshAlarmsAndShowSnackBar()
                    }
                }
            }.show(parentFragmentManager, "")
            true
        }

        preference(ALARM_DAYS_PREFERENCE)?.setOnPreferenceClickListener {
            val savedList = viewModel.reminder.value?.alarmDays
            val options = getSavedOptionsArray(savedList, alarmDaysString)

            OptionsSheet().build(requireContext()) {
                title(this@PreferencesFragment.getString(R.string.alarm_days_title))
                multipleChoices(true)
                with(*options)
                onPositiveMultiple(this@PreferencesFragment.getString(R.string.save_button))
                { selectedIndices , _ ->
                    lifecycleScope.launch {
                        viewModel.saveAlarmDays()
                        refreshAlarmsAndShowSnackBar()
                    }
                }
            }.show(parentFragmentManager, "")
            true
        }
    }

    private fun refreshAlarmsAndShowSnackBar(){
        if(viewModel.isAppActive()) viewModel.refreshAlarms()
        createSnackBar(getString(R.string.new_config_saved_toast))
    }
}
