package com.puntogris.posture.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentMainBinding
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.Constants.SHAKE_ANIMATION_DURATION
import com.puntogris.posture.utils.Utils.minutesFromMidnightToHourlyTime
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment: BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels()

    @Inject lateinit var sharedPref: SharedPref

    override fun initializeViews() {
        if (sharedPref.showAnnouncementPref()) binding.announcementCard.visible()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainFragment = this
    }

    fun onCloseCardButtonClicked(){
        sharedPref.hideAnnouncementPref()
        binding.announcementCard.gone()
    }

    fun onOpenAnnouncementClicked(){
        findNavController().navigate(R.id.announcementFragment)
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
        binding.enableSummaryTextview.playShakeAnimation()
        binding.enableTextView.playShakeAnimation()
    }
}