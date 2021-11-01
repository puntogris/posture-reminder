package com.puntogris.posture.ui.ticket

import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetTicketBinding
import com.puntogris.posture.ui.base.BaseBindingBottomSheetFragment
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.Constants.DATA_KEY
import com.puntogris.posture.utils.Constants.SEND_TICKET_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TicketBottomSheet : BaseBindingBottomSheetFragment<BottomSheetTicketBinding>(R.layout.bottom_sheet_ticket, true) {

    private val viewModel: TicketViewModel by viewModels()

    override fun initializeViews() {
        binding.bottomSheet = this
        setupTicketTypeAdapter()
    }

    private fun setupTicketTypeAdapter(){
        val items = resources.getStringArray(R.array.ticket_types)
        binding.ticketType.apply {
            setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, items))
            setOnItemClickListener { _, _, i, _ ->
                viewModel.updateTicketType(i)
            }
        }
    }

    fun onSendTicketClicked(){
        showProgressUi()
        lifecycleScope.launch {
            when(viewModel.sendTicket(binding.messageText.text.toString())){
                SimpleResult.Failure -> showErrorUi()
                SimpleResult.Success -> navigateBackAndShowSuccessMessage()
            }
            dismiss()
        }
    }

    private fun showProgressUi(){
        binding.apply {
            sendButton.isEnabled = false
            ticketAlert.gone()
            progressBar.visible()
        }
    }

    private fun showErrorUi(){
        binding.apply {
            sendButton.isEnabled = true
            ticketAlert.visible()
            progressBar.gone()
        }
        showSnackBar(R.string.snack_general_error)
    }

    private fun navigateBackAndShowSuccessMessage(){
        setFragmentResult(SEND_TICKET_KEY, bundleOf(DATA_KEY to true))
        findNavController().navigateUp()
    }

    fun onHideKeyboardClicked(){
        hideKeyboard()
    }
}