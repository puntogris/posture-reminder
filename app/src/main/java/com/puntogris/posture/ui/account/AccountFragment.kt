package com.puntogris.posture.ui.account

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentAccountBinding
import com.puntogris.posture.utils.extensions.navigateTo
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

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private val viewModel: AccountViewModel by viewModels()
    private val binding by viewBinding(FragmentAccountBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // we need to do init this or it will crash
        binding.barChart.setBarChartLabels(emptyList())
        binding.donutChart.setDonutChartProgress(0)

        viewModel.user.observe(viewLifecycleOwner) {
            binding.experienceForNextLvl.setExpForNextLevel(it.experience)
            binding.currentLvl.setDonutLevel(it.experience)
            binding.expFromTotalLvl.setExpFromTotalLevel(it.experience)
            binding.donutChart.setDonutChartProgress(it.experience)
            binding.headerLayout.accountHeaderUsername.setUsernameOrPlaceHolder(it.username)
            binding.headerLayout.accountHeaderUserLevel.setAccountLevelTitle(it.experience)
            binding.headerLayout.accountHeaderUserLevelTag.setAccountBadgeLevel(it.experience)
        }

        viewModel.weekData.observe(viewLifecycleOwner) {
            binding.barChart.setBarChartLabels(it)
        }

        binding.manageRemindersButton.setOnClickListener {
            navigateTo(R.id.manageRemindersBottomSheet)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.showItem(R.id.settings)
        menu.showItem(R.id.newReminder)
        super.onCreateOptionsMenu(menu, inflater)
    }
}