package com.puntogris.posture.ui.account

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentAccountBinding
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.getDayStringFormatted
import com.puntogris.posture.utils.gone
import com.puntogris.posture.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@AndroidEntryPoint
class AccountFragment : BaseFragmentOptions<FragmentAccountBinding>(R.layout.fragment_account) {

    private val viewModel: AccountViewModel by viewModels()

    override fun initializeViews() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        observeActiveReminderAndHandleResult()
        setupBarChart()

    }

    private fun observeActiveReminderAndHandleResult(){
        viewModel.getActiveReminder().observe(viewLifecycleOwner){
            handleResultActiveReminder(it)
        }
    }

    private fun handleResultActiveReminder(reminder: Reminder?){
        if (reminder != null) showActiveReminderUi(reminder)
        else showNoActiveReminderFoundUi()
    }

    private fun showActiveReminderUi(reminder: Reminder){
        println(reminder)
        binding.apply {
            activeReminder.reminder = reminder
            activeReminder.root.visible()
            binding.reminderNotFoundGroup.gone()
        }
    }

    private fun showNoActiveReminderFoundUi(){
        binding.apply {
            activeReminder.root.gone()
            reminderNotFoundGroup.visible()
        }
    }

    private fun setupBarChart(){
        lifecycleScope.launch {
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
            val dayString = if (i == 0L) "Hoy" else day.getDayStringFormatted()

            val roomEntry = data.singleOrNull{ it.date == day.toString()}
            val value = roomEntry?.expGained?.toFloat() ?: 0F
            labels.add(dayString to value)
        }
        return labels
    }

    fun onNavigateToRemindersClicked(){
        findNavController().navigate(R.id.manageRemindersBottomSheet)
    }

    fun onNavigateToEditReminder(reminder: Reminder){
        val action = AccountFragmentDirections.actionAccountFragmentToNewReminderBottomSheet(reminder)
        findNavController().navigate(action)
    }
}