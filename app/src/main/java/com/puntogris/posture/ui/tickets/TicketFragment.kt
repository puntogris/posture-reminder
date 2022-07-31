package com.puntogris.posture.ui.tickets

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentTicketBinding
import com.puntogris.posture.ui.settings.SettingsViewModel
import com.puntogris.posture.utils.Result
import com.puntogris.posture.utils.extensions.data
import com.puntogris.posture.utils.extensions.hideKeyBoard
import com.puntogris.posture.utils.extensions.showSnackBar
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TicketFragment : Fragment(R.layout.fragment_ticket) {

    private val binding by viewBinding(FragmentTicketBinding::bind)

    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            buttonTicketFragmentSend.setOnClickListener {
                hideKeyBoard()
                viewModel.sendTicket(editTextTicketFragmentMessage.data)
                    .onEach(::handleTicketResult)
                    .launchIn(lifecycleScope)
            }
            editTextTicketFragmentMessage.doAfterTextChanged {
                buttonTicketFragmentSend.isEnabled = !it.isNullOrEmpty()
            }
        }
    }

    private fun handleTicketResult(result: Result<Unit>) {
        when (result) {
            is Result.Error -> showSnackBar(result.error, binding.buttonTicketFragmentSend)
            is Result.Success -> {
                showSnackBar(R.string.ticket_sent_success)
                findNavController().navigateUp()
            }
        }
        binding.buttonTicketFragmentSend.isEnabled = result !is Result.Loading
        binding.progressTicketFragment.isVisible = result is Result.Loading
    }
}
