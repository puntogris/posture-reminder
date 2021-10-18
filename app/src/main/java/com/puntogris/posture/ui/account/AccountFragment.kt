package com.puntogris.posture.ui.account

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentAccountBinding
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.*
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate

@AndroidEntryPoint
class AccountFragment : BaseFragmentOptions<FragmentAccountBinding>(R.layout.fragment_account) {

    private val viewModel: AccountViewModel by viewModels()

    override fun initializeViews() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupBarChart()
    }

    private fun setupBarChart(){
        launchAndRepeatWithViewLifecycle {
            val data = viewModel.getWeekData()
            val barSet = lastWeekLabels(data)
            binding.barChart.animate(barSet)
        }
    }

    private fun lastWeekLabels(data: List<DayLog>): List<Pair<String, Float>>{
        val today = LocalDate.now()
        val labels = mutableListOf<Pair<String, Float>>()

        for (i in 6 downTo 0L){
            val day = today.minusDays(i)
            val dayString = if (i == 0L) getString(R.string.today) else day.getDayStringFormatted()

            val roomEntry = data.singleOrNull{ it.dateId == day.toString()}
            val value = roomEntry?.expGained?.toFloat() ?: 0F
            labels.add(dayString to value)
        }
        return labels
    }

    fun onNavigateToRemindersClicked(){
        navigateTo(R.id.manageRemindersBottomSheet)
    }

}