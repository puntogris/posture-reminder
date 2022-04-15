package com.puntogris.posture.ui.settings.credits

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.puntogris.posture.databinding.BottomSheetCreditsBinding
import com.puntogris.posture.utils.CreditItem
import com.puntogris.posture.utils.extensions.launchWebBrowserIntent
import com.puntogris.posture.utils.extensions.setupAsFullScreen
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class CreditsBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(BottomSheetCreditsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CreditsAdapter { onCreditClicked(it) }.also {
            binding.creditsList.adapter = it
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun onCreditClicked(creditItem: CreditItem) {
        launchWebBrowserIntent(getString(creditItem.url))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setupAsFullScreen(isDraggable = true)
        }
    }
}
