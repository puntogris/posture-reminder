package com.puntogris.posture.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.play.core.review.ReviewManagerFactory
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import com.puntogris.posture.databinding.BottomSheetCheckpointBinding
import com.puntogris.posture.utils.extensions.UiInterface
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CheckpointBottomSheet : BottomSheetDialogFragment() {

    private val binding by viewBinding(BottomSheetCheckpointBinding::bind)
    private var launchedInAppReviewFlow = false

    @Inject
    lateinit var dataStoreHelper: DataStoreHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_checkpoint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonCreateTicket.setOnClickListener {
            findNavController().navigate(R.id.ticketFragment)
        }
        binding.buttonRateApp.setOnClickListener {
            launchInAppReviewFlow()
        }
    }

    private fun launchInAppReviewFlow() {
        launchedInAppReviewFlow = true
        lifecycleScope.launch {
            try {
                val manager = ReviewManagerFactory.create(requireContext())
                val request = manager.requestReviewFlow().await()
                manager.launchReviewFlow(requireActivity(), request).await()
                dataStoreHelper.disableCheckpoint()
                UiInterface.showSnackBar(getString(R.string.rate_app_success))
            } catch (e: Exception) {
                Timber.e(e.localizedMessage)
                dataStoreHelper.setExpToRetryCheckpoint()
                UiInterface.showSnackBar(getString(R.string.rate_app_error))
            }
            dismiss()
        }
    }

    override fun dismiss() {
        lifecycleScope.launch {
            if (!launchedInAppReviewFlow) {
                dataStoreHelper.setExpToRetryCheckpoint()
            }
            super.dismiss()
        }
    }
}
