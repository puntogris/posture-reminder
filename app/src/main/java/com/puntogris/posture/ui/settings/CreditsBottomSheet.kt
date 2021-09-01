package com.puntogris.posture.ui.settings

import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetCreditsBinding
import com.puntogris.posture.ui.base.BaseBottomSheetFragment
import com.puntogris.posture.utils.launchWebBrowserIntent

class CreditsBottomSheet: BaseBottomSheetFragment<BottomSheetCreditsBinding>(R.layout.bottom_sheet_credits, true) {

    override fun initializeViews() {
        binding.bottomSheet = this
    }

    fun onUiconsCreditClicked(){
        launchWebBrowserIntent("https://www.flaticon.com/uicons")
    }

    fun onPandaCreditClicked(){
        launchWebBrowserIntent("https://lottiefiles.com/ucilucil")
    }
}