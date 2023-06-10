package com.puntogris.posture.ui.reminders.configuration.pickers

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.databinding.DialogColorPickerBinding
import com.puntogris.posture.utils.GridItemDecorator
import com.puntogris.posture.utils.viewBinding

private const val RECYCLER_SPAN_COUNT = 5

class ColorPickerDialog(private val onPickedAction: (Int) -> Unit) : DialogFragment() {

    private val binding by viewBinding(DialogColorPickerBinding::inflate)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding.recyclerViewColors.apply {
            post {
                addItemDecoration(GridItemDecorator(true))
                layoutManager = GridLayoutManager(requireContext(), RECYCLER_SPAN_COUNT)
                adapter = ColorPickerAdapter(::onColorPicked)
            }
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.color_picker_title)
            .setView(binding.root)
            .setNegativeButton(R.string.action_cancel, null)
            .create()
    }

    private fun onColorPicked(color: Int) {
        onPickedAction(color)
        dismiss()
    }
}
