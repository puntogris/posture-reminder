package com.puntogris.posture.ui.settings

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogEditUsernameBinding
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.DATA_KEY
import com.puntogris.posture.utils.constants.Constants.EDIT_NAME_KEY
import com.puntogris.posture.utils.extensions.data
import com.puntogris.posture.utils.extensions.onPositive
import com.puntogris.posture.utils.isSuccess
import com.puntogris.posture.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditUserNameDialog : DialogFragment() {

    private val binding by viewBinding(DialogEditUsernameBinding::inflate)
    private val viewModel: SettingsViewModel by viewModels()
    private val args: EditUserNameDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding.editTextUsernameInput.setText(args.username)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.change_name_title)
            .setView(binding.root)
            .setPositiveButton(R.string.action_done, null)
            .setNegativeButton(R.string.action_cancel) { _, _ -> dismiss() }
            .create()
            .also {
                it.onPositive(::onPositiveButtonClicked)
            }
    }

    private fun onPositiveButtonClicked() {
        val name = binding.editTextUsernameInput.data
        if (name != args.username) {
            showLoadingUi()
            updateUsernameAndHandleResult(name)
        } else {
            dismiss()
        }
    }

    private fun updateUsernameAndHandleResult(name: String) {
        lifecycleScope.launch {
            handleEditNameResult(viewModel.updateUserName(name))
        }
    }

    private fun handleEditNameResult(result: SimpleResult) {
        setFragmentResult(EDIT_NAME_KEY, bundleOf(DATA_KEY to result.isSuccess()))
        findNavController().navigateUp()
    }

    private fun showLoadingUi() {
        binding.apply {
            progressBar.isVisible = true
            editTextUsernameInput.isVisible = false
        }
    }
}
