package com.puntogris.posture.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.puntogris.posture.R
import com.puntogris.posture.databinding.BottomSheetSettingsBinding
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.ui.base.BaseBottomSheetFragment
import com.puntogris.posture.utils.*
import com.puntogris.posture.utils.Constants.DATA_KEY
import com.puntogris.posture.utils.Constants.EDIT_NAME_KEY
import com.puntogris.posture.utils.Constants.SEND_TICKET_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsBottomSheet : BaseBottomSheetFragment<BottomSheetSettingsBinding>(R.layout.bottom_sheet_settings, true) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun initializeViews() {
        binding.bottomSheet = this

        childFragmentManager
            .beginTransaction()
            .replace(binding.container.id, PreferencesFragment())
            .commit()

        setFragmentResultsListener()
    }

    private fun onRateAppClicked(){
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=com.puntogris.posture")))
        }catch (e:Exception){
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + "com.puntogris.posture")
                )
            )
        }
    }

    private fun setFragmentResultsListener(){
        setFragmentResultListener(SEND_TICKET_KEY){ _, bundle ->
            val ticketSentSuccessfully = bundle.getBoolean(DATA_KEY)
            val snackMessage =
                if (ticketSentSuccessfully) R.string.snack_send_ticket_success
                else R.string.snack_connection_error
            showSnackBar(snackMessage)
        }
        setFragmentResultListener(EDIT_NAME_KEY){_, bundle ->
            val editUsernameSuccessfully = bundle.getBoolean(DATA_KEY)
            val snackMessage =
                if (editUsernameSuccessfully) R.string.snack_edit_username_success
                else R.string.snack_connection_error
            showSnackBar(snackMessage)
        }
    }
}

