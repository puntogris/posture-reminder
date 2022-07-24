package com.puntogris.posture.ui.reminders.tutorial

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.databinding.DialogManageReminderTutorialBinding
import com.puntogris.posture.utils.extensions.isDarkThemeOn
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ManageReminderTutorialDialog: DialogFragment() {

    @Inject
    lateinit var dataStore: DataStore

    private val binding by viewBinding(DialogManageReminderTutorialBinding::inflate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initViews()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.tutorial_title)
            .setMessage(R.string.tutorial_content)
            .setView(binding.root)
            .setCancelable(false)
            .create().also {
                it.setCanceledOnTouchOutside(false)
            }
    }

    private fun initViews() {
        binding.buttonReminderTutorialUnderstoodButton.setOnClickListener {
            lifecycleScope.launch {
                dataStore.setShowTutorial(false)
                dismiss()
            }
        }
        binding.swipeAnimation.setAnimation(
            if (isDarkThemeOn()) R.raw.swipe_light else R.raw.swipe_dark
        )
    }
}
