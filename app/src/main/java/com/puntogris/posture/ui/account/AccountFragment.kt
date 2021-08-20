package com.puntogris.posture.ui.account

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentAccountBinding
import com.puntogris.posture.model.DayLog
import com.puntogris.posture.ui.base.BaseFragmentOptions
import com.puntogris.posture.utils.getDayStringFormatted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@AndroidEntryPoint
class AccountFragment : BaseFragmentOptions<FragmentAccountBinding>(R.layout.fragment_account) {

    private val viewModel: AccountViewModel by viewModels()

    override fun initializeViews() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.selectReminder.itemName.text = "Recordatorio activo"
        binding.selectReminder.itemDescription.text = "Recordatorio1"
        binding.selectReminder.layout.setOnClickListener {
            findNavController().navigate(R.id.manageRemindersBottomSheet)
        }

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
}