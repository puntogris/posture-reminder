package com.puntogris.posture.ui.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentAccountBinding
import com.puntogris.posture.utils.extensions.launchAndRepeatWithViewLifecycle
import com.puntogris.posture.utils.extensions.showItem
import com.puntogris.posture.utils.setAccountBadgeLevel
import com.puntogris.posture.utils.setAccountLevelTitle
import com.puntogris.posture.utils.setBarChartLabels
import com.puntogris.posture.utils.setDonutChartProgress
import com.puntogris.posture.utils.setDonutLevel
import com.puntogris.posture.utils.setExpForNextLevel
import com.puntogris.posture.utils.setExpFromTotalLevel
import com.puntogris.posture.utils.setUsernameOrPlaceHolder
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account), MenuProvider {

    private val viewModel: AccountViewModel by viewModels()
    private val binding by viewBinding(FragmentAccountBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupViews() {
        setDonutChartProgress(binding.donutChart, 0)
        binding.buttonManageReminders.setOnClickListener {
            findNavController().navigate(R.id.manageRemindersFragment)
        }
    }

    private fun setupObservers() {
        launchAndRepeatWithViewLifecycle {
            viewModel.user.collectLatest {
                with(binding) {
                    setExpForNextLevel(textViewExperienceForNextLevel, it.experience)
                    setDonutLevel(textViewCurrentLevel, it.experience)
                    setExpFromTotalLevel(textViewExpFromTotalLevel, it.experience)
                    setDonutChartProgress(donutChart, it.experience)
                }
                with(binding.headerLayout) {
                    setUsernameOrPlaceHolder(textViewHeaderUsername, it.username)
                    setAccountLevelTitle(textViewHeaderLevel, it.experience)
                    setAccountBadgeLevel(textViewHeaderLevelTag, it.experience)
                }
            }
        }
        launchAndRepeatWithViewLifecycle(Lifecycle.State.CREATED) {
            viewModel.showCheckpoint.collectLatest { show ->
                if (show) {
                    findNavController().navigate(R.id.checkpointBottomSheet)
                }
            }
        }
        launchAndRepeatWithViewLifecycle {
            viewModel.weekData.collectLatest {
                setBarChartLabels(binding.barChart, it)
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.showItem(R.id.settings)
        menu.showItem(R.id.newReminder)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
}
