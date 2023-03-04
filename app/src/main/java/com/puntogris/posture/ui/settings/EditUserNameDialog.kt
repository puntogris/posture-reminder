package com.puntogris.posture.ui.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogEditUsernameBinding
import com.puntogris.posture.utils.extensions.data
import com.puntogris.posture.utils.viewBinding

class EditUserNameDialog(
    private val currentName: String,
    private val onPositiveAction: (String) -> Unit
) : DialogFragment() {

    private val binding by viewBinding(DialogEditUsernameBinding::inflate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding.editTextUsernameInput.setText(currentName)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.change_name_title)
            .setView(binding.root)
            .setPositiveButton(R.string.action_done) { _, _ ->
                onPositiveAction(binding.editTextUsernameInput.data)
            }
            .setNegativeButton(R.string.action_cancel) { _, _ -> dismiss() }
            .create()
    }
}
