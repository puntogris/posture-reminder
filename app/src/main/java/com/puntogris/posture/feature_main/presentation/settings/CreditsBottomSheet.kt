package com.puntogris.posture.feature_main.presentation.settings

import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetCreditsBinding
import com.puntogris.posture.common.presentation.base.BaseBindingBottomSheetFragment
import com.puntogris.posture.common.utils.launchWebBrowserIntent

class CreditsBottomSheet :
    BaseBindingBottomSheetFragment<BottomSheetCreditsBinding>(R.layout.bottom_sheet_credits, true) {

    override fun initializeViews() {
        binding.bottomSheet = this
    }

    fun onUiconsCreditClicked() {
        launchWebBrowserIntent("https://www.flaticon.com/uicons")
    }

    fun onPandaCreditClicked() {
        launchWebBrowserIntent("https://lottiefiles.com/ucilucil")
    }
}