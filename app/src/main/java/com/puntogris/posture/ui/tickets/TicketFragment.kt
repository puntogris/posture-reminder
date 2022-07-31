package com.puntogris.posture.ui.tickets

import androidx.fragment.app.Fragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.FragmentTicketBinding
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketFragment: Fragment(R.layout.fragment_ticket) {

    private val binding by viewBinding(FragmentTicketBinding::bind)


}