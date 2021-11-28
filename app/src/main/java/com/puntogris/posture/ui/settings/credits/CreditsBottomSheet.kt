package com.puntogris.posture.ui.settings.credits

import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetCreditsBinding
import com.puntogris.posture.ui.base.BaseBindingBottomSheetFragment
import com.puntogris.posture.utils.CreditItem
import com.puntogris.posture.utils.launchWebBrowserIntent

class CreditsBottomSheet :
    BaseBindingBottomSheetFragment<BottomSheetCreditsBinding>(R.layout.bottom_sheet_credits, true) {

    override fun initializeViews() {
        binding.bottomSheet = this

        CreditsAdapter{ onCreditClicked(it) }.also {
            binding.creditsList.adapter = it
        }
    }

    private fun onCreditClicked(creditItem: CreditItem){
        launchWebBrowserIntent(getString(creditItem.url))
    }
}
