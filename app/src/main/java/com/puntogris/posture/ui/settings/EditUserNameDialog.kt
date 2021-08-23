package com.puntogris.posture.ui.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogEditUsernameBinding
import com.puntogris.posture.model.SimpleResult
import com.puntogris.posture.utils.Constants.DATA_KEY
import com.puntogris.posture.utils.Constants.EDIT_NAME_KEY
import com.puntogris.posture.utils.gone
import com.puntogris.posture.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditUserNameDialog: DialogFragment(){

    private lateinit var binding: DialogEditUsernameBinding
    private val viewModel: SettingsViewModel by viewModels()
    private val args: EditUserNameDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_edit_username, null, false)
        binding.dialog = this
        binding.usernameEditText.setText(args.username)

        return MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_rounded)
                .setView(binding.root)
                .setPositiveButton(R.string.action_done, null)
                .setNegativeButton(R.string.action_cancel) { _, _ -> dismiss() }
                .create()
            .also {
                it.setOnShowListener { _ ->
                    it.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        onPositiveButtonClicked()
                    }
                }
            }
    }

    private fun onPositiveButtonClicked(){
        val name = binding.usernameEditText.text.toString()
        if (name != args.username){
            showLoadingUi()
            updateUsernameAndHandleResult(name)
        } else dismiss()
    }

    private fun updateUsernameAndHandleResult(name: String){
        lifecycleScope.launch {
            val result = when (viewModel.updateUserName(name)) {
                SimpleResult.Failure -> false
                SimpleResult.Success -> true
            }
            handleEditNameResult(result)
        }
    }

    private fun handleEditNameResult(result: Boolean){
        setFragmentResult(EDIT_NAME_KEY, bundleOf(DATA_KEY to result))
        findNavController().navigateUp()
    }

    private fun showLoadingUi(){
        binding.apply {
            progressBar.visible()
            usernameEditText.gone()
        }
    }
}