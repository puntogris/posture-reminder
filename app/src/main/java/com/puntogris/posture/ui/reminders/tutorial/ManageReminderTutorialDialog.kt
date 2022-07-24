package com.puntogris.posture.ui.reminders.tutorial

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogManageReminderTutorialBinding
import com.puntogris.posture.utils.extensions.isDarkThemeOn
import com.puntogris.posture.utils.viewBinding

class ManageReminderTutorialDialog: DialogFragment() {

    private val binding by viewBinding(DialogManageReminderTutorialBinding::inflate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding.buttonReminderTutorialUnderstoodButton.setOnClickListener {
            dismiss()
        }
        binding.swipeAnimation.setAnimation(
            if (isDarkThemeOn()) R.raw.swipe_light else R.raw.swipe_dark
        )

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Administra tus recordatorios")
            .setMessage("Deslizando hacia los costados podras eliminar y editar tu recordatorio.")
            .setView(binding.root)
            .setCancelable(false)
            .create()
    }
}
