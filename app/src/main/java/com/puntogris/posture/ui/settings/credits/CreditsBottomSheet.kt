package com.puntogris.posture.ui.settings.credits

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetCreditsBinding
import com.puntogris.posture.utils.extensions.launchWebBrowserIntent
import com.puntogris.posture.utils.extensions.setupAsFullScreen
import com.puntogris.posture.utils.viewBinding

class CreditsBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(BottomSheetCreditsBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_credits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCredits.adapter = CreditsAdapter { launchWebBrowserIntent(it.url) }
        binding.imageViewClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setupAsFullScreen(isDraggable = true)
        }
    }
}
