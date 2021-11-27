package com.puntogris.posture.feature_main.presentation.ticket

import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetTicketBinding
import com.puntogris.posture.common.presentation.base.BaseBindingBottomSheetFragment
import com.puntogris.posture.common.utils.Result
import com.puntogris.posture.common.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.common.utils.constants.Constants.SEND_TICKET_KEY
import com.puntogris.posture.common.utils.gone
import com.puntogris.posture.common.utils.hideKeyboard
import com.puntogris.posture.common.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TicketBottomSheet :
    BaseBindingBottomSheetFragment<BottomSheetTicketBinding>(R.layout.bottom_sheet_ticket, true) {

    private val viewModel: TicketViewModel by viewModels()

    override fun initializeViews() {
        binding.bottomSheet = this
        binding.viewModel = viewModel
        setupTicketTypeAdapter()
    }

    private fun setupTicketTypeAdapter() {
        binding.ticketType.apply {
            setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    resources.getStringArray(R.array.ticket_types)
                )
            )
            setOnItemClickListener { _, _, i, _ ->
                viewModel.updateTicketType(i)
            }
        }
    }

    fun onSendTicketClicked() {
        lifecycleScope.launch {
            viewModel.sendTicket().collect {
                with(binding) {
                    when (it) {
                        is Result.Error -> {
                            sendTicketButton.isEnabled = true
                            ticketAlert.visible()
                            progressBar.gone()
                        }
                        is Result.Loading -> {
                            sendTicketButton.isEnabled = false
                            ticketAlert.gone()
                            progressBar.visible()
                        }
                        is Result.Success -> {
                            setFragmentResult(SEND_TICKET_KEY, bundleOf(DATA_KEY to true))
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    fun onHideKeyboardClicked() {
        hideKeyboard()
    }
}