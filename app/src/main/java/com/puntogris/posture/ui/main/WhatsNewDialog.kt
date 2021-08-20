package com.puntogris.posture.ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.BuildConfig
import com.puntogris.posture.R
import com.puntogris.posture.utils.launchWebBrowserIntent

class WhatsNewDialog: DialogFragment() {

    override fun onCreateDialog(savedViewState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.MyAlertDialogStyle)
            .setTitle(getString(R.string.updated_version, BuildConfig.VERSION_NAME))
            .setPositiveButton(android.R.string.ok, null)
            .setNeutralButton(R.string.whats_new) { _, _ ->
                launchWebBrowserIntent("https://postureapp.puntogris.com/releases/tag/v${BuildConfig.VERSION_NAME}")
            }
            .create()
    }
}
