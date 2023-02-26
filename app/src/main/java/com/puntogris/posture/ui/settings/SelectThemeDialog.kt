package com.puntogris.posture.ui.settings

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.posture.R
import com.puntogris.posture.data.datasource.local.DataStoreHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SelectThemeDialog : DialogFragment() {

    @Inject
    lateinit var dataStoreHelper: DataStoreHelper

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val currentPosition = runBlocking {
            val currentTheme = dataStoreHelper.appTheme()
            if (currentTheme == -1) 2 else currentTheme - 1
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.theme)
            .setSingleChoiceItems(R.array.theme_names, currentPosition) { _, position ->
               setThemePreference(position)
            }
            .create()
    }
    private fun setThemePreference(position: Int) {
        val themeValuesArray = resources.getIntArray(R.array.theme_values)
        themeValuesArray[position].let {
            lifecycleScope.launch {
                dataStoreHelper.setAppTheme(it)
                AppCompatDelegate.setDefaultNightMode(it)
                dismiss()
            }
        }
    }
}
