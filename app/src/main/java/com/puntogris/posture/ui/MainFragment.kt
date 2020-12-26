package com.puntogris.posture.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentMainBinding
import com.puntogris.posture.utils.Utils.minutesFromMidnightToHourlyTime
import com.puntogris.posture.utils.createNotificationChannel
import com.puntogris.posture.utils.createSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels()

    override fun initializeViews() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainFragment = this
        createNotificationChannel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.preferencesFragment).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onScreenClicked(){
        if (viewModel.isAppActive()) {
            viewModel.cancelAlarms()
            createSnackBar(getString(R.string.alarms_off_toast))
        }
        else {
            viewModel.startAlarm()
            createSnackBar(getString(
                R.string.notifications_set_toast,
                minutesFromMidnightToHourlyTime(viewModel.reminder.value!!.startTime),
                minutesFromMidnightToHourlyTime(viewModel.reminder.value!!.endTime)
            ))
        }
    }
}